package com.mekari.mokaaddons.common.webhook.moka;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.handler.RequestHandler;
import com.mekari.mokaaddons.common.util.DateUtil;
import com.mekari.mokaaddons.common.webhook.LockTrackerStorage;
import com.mekari.mokaaddons.common.webhook.LockTrackerStorage.NewItem;

import org.springframework.util.Assert;

public class EventLockHandler<TRequest extends EventRequest> extends AbstractVoidRequestHandler<TRequest> {

    private final DataSource dataSource;
    private final LockTrackerStorage lockTracker;
    private final RequestHandler<TRequest, Void> next;

    private static final String GET_CONNID_EVID_SQL = "SELECT connection_id() id UNION (SELECT id FROM event_source WHERE data_id=? LIMIT 1);";
    private static final String LOCKING_ROW_SQL = "SELECT id FROM event_source WHERE id = %s FOR UPDATE;";

    public EventLockHandler(DataSource dataSource, LockTrackerStorage lockTracker, RequestHandler<TRequest, Void> next) {
        Assert.notNull(dataSource, "dataSource must not be null");
        Assert.notNull(lockTracker, "lockTracker must not be null");
        Assert.notNull(next, "next must not be null");

        this.dataSource = dataSource;
        this.lockTracker = lockTracker;
        this.next = next;
    }

    @Override
    protected void handleInternal(TRequest request) throws Exception {
        var event = request.getEvent();
        try (var conn = createConnection(event)) {
            // we can track a rowlock created by this connection through:
            // select * from INFORMATION_SCHEMA.INNODB_TRX where trx_mysql_thread_id =
            // connId
            var lockItem = lock(conn, event);
            try {
                next.handle(request);
            } finally {
                releaseLock(lockItem, conn, event);
            }
        }
    }
    private Object[] getConnIdAndEventSourceId(Connection conn, AbstractEvent event) throws Exception {
        try (var stmt = conn.prepareStatement(GET_CONNID_EVID_SQL)) {
            stmt.setString(1, event.getBody().getData().getId().toString());
            try (var rs = stmt.executeQuery()) {
                rs.next();
                var connId = rs.getInt(1);
                if (!rs.next())
                    throw new EventSourceNotFoundException(event);
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
                header.getEventId(), header.getEventName(), body.getData().getId(), connId, query);

        try (var stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
            stmt.setPoolable(false);
            try (var rs = stmt.executeQuery(query)) {
                if (!rs.next())
                    throw new EventSourceNotFoundException(
                            String.format("eventId:%s-eventName:%s-bodyId:%s no event_source with id:%s",
                                    header.getEventId(), header.getEventName(), body.getData().getId(), evsId),
                            event);
            }
        }

        var lockItem = NewItem.builder()
                .connId(connId)
                .eventId(header.getEventId())
                .eventName(header.getEventName())
                .dataId(body.getData().getId().toString())
                .query(query)
                .createdAt(DateUtil.now())
                .build();

        try {
            lockTracker.insert(lockItem);
        } catch (Exception ex) {
            logger.error(ex.toString());
        }

        logger.info("###eventId:%s-eventName:%s-bodyId:%s [successfully] acquire a row locking through connId:%d", 
                header.getEventId(), header.getEventName(), body.getData().getId(), connId);
        return lockItem;
    }

    private void releaseLock(NewItem lockItem, Connection conn, AbstractEvent event) throws Exception {
        var header = event.getHeader();
        var body = event.getBody();

        logger.info("###eventId:%s-eventName:%s-bodyId:%s [releases] a row locking through connId:%d",
                header.getEventId(), header.getEventName(), body.getData().getId(), lockItem.getConnId());

        conn.rollback();
        conn.setAutoCommit(true);

        try {
            lockTracker.delete(lockItem.getConnId(), lockItem.getCreatedAt());
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
    }

    private Connection createConnection(AbstractEvent event) throws SQLException {
        var header = event.getHeader();
        var body = event.getBody();
        
        logger.debug("eventId:%s-eventName:%s-bodyId:%s tries to connect to database for a row locking",
                header.getEventId(), header.getEventName(), body.getData().getId());

        var conn = dataSource.getConnection();
        conn.setAutoCommit(false);

        logger.debug("eventId:%s-eventName:%s-bodyId:%s is successfully connected to db for a row locking",
                header.getEventId(), header.getEventName(), body.getData().getId());

        return conn;
    }
}
