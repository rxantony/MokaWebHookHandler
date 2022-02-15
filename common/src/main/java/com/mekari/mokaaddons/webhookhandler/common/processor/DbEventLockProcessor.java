package com.mekari.mokaaddons.webhookhandler.common.processor;

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

public class DbEventLockProcessor<TEvent extends Event> extends AbstractEventProcessor<TEvent> {

    private final DataSource dataSource;
    private final LockTrackerStorage lockTracker;
    private final EventProcessor<TEvent> innerProcessor;

    private static final String GET_CONNECTIONID_SQL = "SELECT connection_id() id";
    private static final String LOCKING_ROW_SQL = "SELECT id FROM event_source WHERE data_id = %s LIMIT 1 FOR UPDATE";

    public DbEventLockProcessor(DataSource dataSource, LockTrackerStorage lockTracker, EventProcessor<TEvent> innerProcessor) {
        super(innerProcessor.eventClass());

        Assert.notNull(dataSource, "dataSource must not be null");
        Assert.notNull(lockTracker, "lockTracker must not be null");

        this.dataSource = dataSource;
        this.lockTracker = lockTracker;
        this.innerProcessor = innerProcessor;
    }

    @Override
    public void process(TEvent event) throws EventProcessingException {
        try (var conn = createConnection(event);) {
            // we can track a rowlock created by this connection through:
            // select * from INFORMATION_SCHEMA.INNODB_TRX where trx_mysql_thread_id =
            // connId
            var lockItem = lock(conn, event);
            try {
                innerProcessor.process(event);
            } finally {
                releaseLock(lockItem, conn, event);
            }
        } catch (Exception ex) {
            throw new EventProcessingException(ex);
        }
    }

    private int getConncctionId(Connection conn) throws SQLException {
        var stmt = conn.createStatement();
        var rs = stmt.executeQuery(GET_CONNECTIONID_SQL);
        rs.next();
        return rs.getInt(1);
    }

    private LockTrackerStorage.Item lock(Connection conn, TEvent event) throws Exception {
        var header = event.getHeader();
        var data = event.getBody().getData();
        var connId = getConncctionId(conn);
        var query = String.format(LOCKING_ROW_SQL, data.getId());
        var stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

        logger.debug("eventId:%s-dataId:%s tries to acquire a row lock through connId:%d", header.getEventId(),
                data.getId(), connId);

        stmt.executeQuery(query);

        var lockItem = new Item().builder()
                .connId(connId)
                .eventId(event.getHeader().getEventId())
                .dataId(event.getBody().getData().getId())
                .query(query)
                .createdAt(Instant.now().atOffset(ZoneOffset.UTC))
                .build();

        lockTracker.insert(lockItem);
        logger.debug("eventId:%s-dataId:%s successfully acquire a row locking through connId:%d", header.getEventId(),
                data.getId(), connId);
        return lockItem;
    }

    private void releaseLock(Item lockItem, Connection conn, TEvent event) throws Exception {
        var header = event.getHeader();
        var data = event.getBody().getData();

        logger.debug("eventId:%s-dataId:%s releases a row locking through connId:%d", header.getEventId(),
                data.getId(), lockItem.getConnId());

        conn.rollback();
        lockTracker.delete(lockItem.getConnId(), lockItem.getCreatedAt());
    }

    private Connection createConnection(TEvent event) throws SQLException {
        var header = event.getHeader();
        var data = event.getBody().getData();

        logger.debug("eventId:%s-dataId:%s tries to connect to database for a row locking", header.getEventId(),
                data.getId());
        Connection conn = dataSource.getConnection();
        conn.setAutoCommit(false);
        logger.debug("eventId:%s-dataId:%s is successfully connected to db for a row locking", header.getEventId(),
                data.getId());

        return conn;
    }
}
