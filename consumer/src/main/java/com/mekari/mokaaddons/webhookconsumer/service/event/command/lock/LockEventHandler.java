package com.mekari.mokaaddons.webhookconsumer.service.event.command.lock;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.mekari.mokaaddons.common.handler.RequestHandler;
import com.mekari.mokaaddons.common.util.DateUtil;
import com.mekari.mokaaddons.common.webhook.EventHandlingException;
import com.mekari.mokaaddons.common.webhook.LockTrackerStorage;
import com.mekari.mokaaddons.common.webhook.LockTrackerStorage.NewItem;
import com.mekari.mokaaddons.common.webhook.moka.AbstractEvent;
import com.mekari.mokaaddons.common.webhook.util.ExceptionUtil;

@Service
public class LockEventHandler implements RequestHandler<LockEventRequest, LockEventResult> {

    private DataSource dataSource;
    private LockTrackerStorage lockTracker;

    private static final String GET_CONNID_EVID_SQL = "SELECT connection_id() id UNION (SELECT id FROM event_source WHERE data_id=? LIMIT 1);";
    private static final String LOCKING_ROW_SQL = "SELECT id FROM event_source WHERE id = %s FOR UPDATE;";
    private static final Logger logger = LogManager.getFormatterLogger(LockEventHandler.class);

    public LockEventHandler(@Qualifier("eventstore") DataSource dataSource, @Autowired LockTrackerStorage lockTracker){
        this.dataSource = dataSource;
        this.lockTracker = lockTracker;
    }
    @Override
    public LockEventResult handle(LockEventRequest request) throws Exception {
        var event = request.getEvent();
        var conn = createConnection(event);
        NewItem lockInfo = null;
        try {
            lockInfo = lock(conn, event);
            final var ilockInfo = lockInfo;
            return new LockEventResult(event, lockInfo, () -> releaseLock(ilockInfo, conn, event));
        } catch (Exception ex) {
            if (lockInfo != null)
                releaseLock(lockInfo, conn, event);
            throw ex;
        }
    }

    private Object[] getConnIdAndEventSourceId(Connection conn, AbstractEvent event) throws Exception {
        try (var stmt = conn.prepareStatement(GET_CONNID_EVID_SQL)) {
            stmt.setString(1, event.getBody().getId().toString());
            try (var rs = stmt.executeQuery()) {
                rs.next();
                var connId = rs.getInt(1);
                if (!rs.next())
                    throw ExceptionUtil.eventNotFoundInEventSource(event);
                var evsId = rs.getString(1);
                return new Object[] { connId, evsId };
            }
        }
    }

    private NewItem lock(Connection conn, AbstractEvent event) throws Exception {
        var ctx = getConnIdAndEventSourceId(conn, event);
        var connId = (int) ctx[0];
        var evsId = (String) ctx[1];
        var query = String.format(LOCKING_ROW_SQL, evsId);

        var header = event.getHeader();
        var body = event.getBody();

        logger.debug("eventId:%s-eventName:%s-bodyId:%s tries to acquire a row lock through connId:%d with query:[%s]",
                header.getEventId(), header.getEventName(), body.getId(), connId, query);

        try (var stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
            stmt.setPoolable(false);
            try (var rs = stmt.executeQuery(query)) {
                if (!rs.next())
                    throw new EventHandlingException(
                            String.format("eventId:%s-eventName:%s-bodyId:%s no event_source with id:%s",
                                    header.getEventId(), header.getEventName(), body.getId(), evsId),
                            event);
            }
        }

        var lockItem = NewItem.builder()
                .connId(connId)
                .eventId(header.getEventId())
                .eventName(header.getEventName())
                .dataId(body.getId().toString())
                .query(query)
                .createdAt(DateUtil.utcNow())
                .build();

        try {
            lockTracker.insert(lockItem);
        } catch (Exception ex) {
            logger.error(ex.toString());
        }

        logger.info("###eventId:%s-eventName:%s-bodyId:%s [successfully] acquire a row locking through connId:%d",
                header.getEventId(), header.getEventName(), body.getId(), connId);
        return lockItem;
    }

    private void releaseLock(NewItem lockItem, Connection conn, AbstractEvent event) throws Exception {
        var header = event.getHeader();
        var body = event.getBody();

        logger.info("###eventId:%s-eventName:%s-bodyId:%s [releases] a row locking through connId:%d",
                header.getEventId(), header.getEventName(), body.getId(), lockItem.getConnId());
        
        try {
            conn.rollback();
            conn.setAutoCommit(true);
        } catch (Exception ex) {
            logger.error(ex.toString());
            throw ex;
        }
        finally{
            try {
                lockTracker.delete(lockItem.getConnId(), lockItem.getCreatedAt());
            } catch (Exception ex) {
                logger.error(ex.toString());
            }
        }
    }

    private Connection createConnection(AbstractEvent event) throws SQLException {
        var header = event.getHeader();
        var body = event.getBody();

        logger.debug("eventId:%s-eventName:%s-bodyId:%s tries to connect to database for a row locking",
                header.getEventId(), header.getEventName(), body.getId());

        var conn = dataSource.getConnection();
        conn.setAutoCommit(false);

        logger.debug("eventId:%s-eventName:%s-bodyId:%s is successfully connected to db for a row locking",
                header.getEventId(), header.getEventName(), body.getId());

        return conn;
    }
}
