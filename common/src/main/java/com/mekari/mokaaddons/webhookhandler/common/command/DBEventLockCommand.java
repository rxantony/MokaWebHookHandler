package com.mekari.mokaaddons.webhookhandler.common.command;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;
import com.mekari.mokaaddons.webhookhandler.common.storage.LockTrackerStorage;
import com.mekari.mokaaddons.webhookhandler.common.storage.LockTrackerStorage.Item;
import com.mekari.mokaaddons.webhookhandler.common.util.DateUtil;

import org.springframework.util.Assert;

public class DBEventLockCommand<TEvent extends Event> extends AbstractEventCommand<TEvent> {

    private final DataSource dataSource;
    private final LockTrackerStorage lockTracker;
    private final EventCommand<TEvent> inner;

    private static final String GET_CONNID_EVID_SQL = "SELECT connection_id() id UNION (SELECT id FROM event_source WHERE data_id=? LIMIT 1);";
    private static final String LOCKING_ROW_SQL = "SELECT id FROM event_source WHERE id = %s FOR UPDATE;";

    public DBEventLockCommand(DataSource dataSource, LockTrackerStorage lockTracker, EventCommand<TEvent> inner) {
        super(inner.eventClass());

        Assert.notNull(dataSource, "dataSource must not be null");
        Assert.notNull(lockTracker, "lockTracker must not be null");
        Assert.notNull(inner, "inner must not be null");

        this.dataSource = dataSource;
        this.lockTracker = lockTracker;
        this.inner = inner;
    }

    @Override
    protected void executeInternal(TEvent event) throws Exception {
        try (var conn = createConnection(event)) {
            // we can track a rowlock created by this connection through:
            // select * from INFORMATION_SCHEMA.INNODB_TRX where trx_mysql_thread_id =
            // connId
            var lockItem = lock(conn, event);
            try {
                inner.execute(event);
            } finally {
                releaseLock(lockItem, conn, event);
            }
        }
    }

    private Object[] getConnIdAndEventSourceId(Connection conn, TEvent event) throws Exception {
        try (var stmt = conn.prepareStatement(GET_CONNID_EVID_SQL)) {
            stmt.setString(1, event.getBody().getId());
            try (var rs = stmt.executeQuery()) {
                rs.next();
                var connId = rs.getInt(1);
                if (!rs.next())
                    throw new EventSourceDataNotFoundException(event);
                var evsId = rs.getString(1);
                return new Object[] { connId, evsId };
            }
        }
    }

    private Item lock(Connection conn, TEvent event) throws Exception {
        var ctx = getConnIdAndEventSourceId(conn, event);
        var connId = (int) ctx[0];
        var evsId = (String) ctx[1];
        var query = String.format(LOCKING_ROW_SQL, evsId);

        logger.debug("eventId:%s-eventName:%s-bodyId:%s tries to acquire a row lock through connId:%d with query:[%s]",
                event.geId(), event.getName(), event.getBody().getId(), connId, query);

        try (var stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
            stmt.setPoolable(false);
            try (var rs = stmt.executeQuery(query)) {
                if (!rs.next())
                    throw new EventSourceDataNotFoundException(
                            String.format("eventId:%s-eventName:%s-bodyId:%s no event_source with id:%s",
                                    event.geId(), event.getName(), event.getBody().getId(), evsId),
                            event);
            }
        }

        var lockItem = Item.builder()
                .connId(connId)
                .eventId(event.geId())
                .eventName(event.getName())
                .dataId(event.getBody().getId())
                .query(query)
                .createdAt(DateUtil.now())
                .build();

        try {
            lockTracker.insert(lockItem);
        } catch (Exception ex) {
            logger.error(ex.toString());
        }

        logger.info("###eventId:%s-eventName:%s-bodyId:%s [successfully] acquire a row locking through connId:%d", 
                event.geId(), event.getName(), event.getBody().getId(), connId);
        return lockItem;
    }

    private void releaseLock(Item lockItem, Connection conn, TEvent event) throws Exception {
        logger.info("###eventId:%s-eventName:%s-bodyId:%s [releases] a row locking through connId:%d",
                event.geId(), event.getName(), event.getBody().getId(), lockItem.getConnId());

        conn.rollback();
        conn.setAutoCommit(true);

        try {
            lockTracker.delete(lockItem.getConnId(), lockItem.getCreatedAt());
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
    }

    private Connection createConnection(TEvent event) throws SQLException {
        logger.debug("eventId:%s-eventName:%s-bodyId:%s tries to connect to database for a row locking",
                event.geId(), event.getName(), event.getBody().getId());

        var conn = dataSource.getConnection();
        conn.setAutoCommit(false);

        logger.debug("eventId:%s-eventName:%s-bodyId:%s is successfully connected to db for a row locking",
                event.geId(), event.getName(), event.getBody().getId());

        return conn;
    }
}
