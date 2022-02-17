package com.mekari.mokaaddons.webhookhandler.common.command;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneOffset;

import javax.sql.DataSource;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;
import com.mekari.mokaaddons.webhookhandler.common.storage.LockTrackerStorage;
import com.mekari.mokaaddons.webhookhandler.common.storage.LockTrackerStorage.Item;

import org.springframework.util.Assert;

public class DBCommandEventLock<TEvent extends Event> extends AbstractCommandEvent<TEvent> {

    private final DataSource dataSource;
    private final LockTrackerStorage lockTracker;
    private final CommandEvent<TEvent> inner;

    private static final String GET_CONNID_EVID_SQL = "SELECT connection_id() id UNION (SELECT id FROM event_source WHERE data_id=? LIMIT 1);";
    private static final String LOCKING_ROW_SQL = "SELECT 1 FROM event_source WHERE id = %s LIMIT 1 FOR UPDATE;";

    public DBCommandEventLock(DataSource dataSource, LockTrackerStorage lockTracker, CommandEvent<TEvent> inner) {
        super(inner.eventClass());

        Assert.notNull(dataSource, "dataSource must not be null");
        Assert.notNull(lockTracker, "lockTracker must not be null");

        this.dataSource = dataSource;
        this.lockTracker = lockTracker;
        this.inner = inner;
    }

    @Override
    public void execute(TEvent event) throws CommandException {
        try (var conn = createConnection(event);) {
            // we can track a rowlock created by this connection through:
            // select * from INFORMATION_SCHEMA.INNODB_TRX where trx_mysql_thread_id =
            // connId
            var lockItem = lock(conn, event);
            try {
                // var header = event.getHeader();
                // var data = event.getBody().getData();
                // logger.debug("###eventId:%s-dataId:%s sleep 5000, connId:%d",
                // header.getEventId(), data.getId(), lockItem.getConnId());
                // Thread.sleep(5000);
                inner.execute(event);
            } finally {
                releaseLock(lockItem, conn, event);
            }
        } catch (CommandException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new CommandException(ex);
        }
    }

    private Object[] getConnIdAndEventSourceId(Connection conn, TEvent event) throws Exception {
        var header = event.getHeader();
        var data = event.getBody().getData();
        try (var stmt = conn.prepareStatement(GET_CONNID_EVID_SQL);) {
            stmt.setString(1, data.getId());
            try (var rs = stmt.executeQuery();) {
                rs.next();
                var connId = rs.getInt(1);

                if (!rs.next())
                    throw new CommandException(String.format("eventId:%s-dataId:%s no event_source with data_id:%s",
                            header.getEventId(), data.getId(), data.getId()));
                var evsId = rs.getString(1);
                return new Object[] { connId, evsId };
            }
        }
    }

    private Item lock(Connection conn, TEvent event) throws Exception {
        var header = event.getHeader();
        var data = event.getBody().getData();
        var ctx = getConnIdAndEventSourceId(conn, event);
        var connId = (int) ctx[0];
        var evsId = (String) ctx[1];
        var query = String.format(LOCKING_ROW_SQL, evsId);

        var stmt = conn.createStatement(ResultSet.CLOSE_CURSORS_AT_COMMIT, ResultSet.CONCUR_UPDATABLE);
        stmt.setPoolable(false);

        logger.debug("eventId:%s-dataId:%s tries to acquire a row lock through connId:%d with query:[%s]",
                header.getEventId(),
                data.getId(), connId, query);

        var rs = stmt.executeQuery(query);
        if (!rs.next())
            throw new CommandException(String.format("eventId:%s-dataId:%s no event_source with id:%s",
                    header.getEventId(), data.getId(), evsId));

        var lockItem = Item.builder()
                .connId(connId)
                .eventId(event.getHeader().getEventId())
                .dataId(event.getBody().getData().getId())
                .query(query)
                .createdAt(Instant.now().atOffset(ZoneOffset.UTC))
                .build();

        try {
            lockTracker.insert(lockItem);
        } catch (Exception ex) {
            logger.error(ex.toString());
        }

        logger.debug("###eventId:%s-dataId:%s [successfully] acquire a row locking through connId:%d",
                header.getEventId(),
                data.getId(), connId);
        return lockItem;
    }

    private void releaseLock(Item lockItem, Connection conn, TEvent event) throws Exception {
        var header = event.getHeader();
        var data = event.getBody().getData();

        logger.debug("###eventId:%s-dataId:%s [releases] a row locking through connId:%d", header.getEventId(),
                data.getId(), lockItem.getConnId());

        // TODO: need to ensure the unlock mechanism for any condition has already been
        // prpepared well
        conn.rollback();
        conn.close();

        try {
            lockTracker.delete(lockItem.getConnId(), lockItem.getCreatedAt());
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
    }

    private Connection createConnection(TEvent event) throws SQLException {
        var header = event.getHeader();
        var data = event.getBody().getData();

        logger.debug("eventId:%s-dataId:%s tries to connect to database for a row locking", header.getEventId(),
                data.getId());

        var conn = dataSource.getConnection();
        conn.setAutoCommit(false);
        
        logger.debug("eventId:%s-dataId:%s is successfully connected to db for a row locking", header.getEventId(),
                data.getId());

        return conn;
    }
}
