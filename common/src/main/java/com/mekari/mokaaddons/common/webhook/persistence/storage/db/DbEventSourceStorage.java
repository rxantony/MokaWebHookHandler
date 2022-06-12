package com.mekari.mokaaddons.common.webhook.persistence.storage.db;

import java.time.OffsetDateTime;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.util.Assert;

import com.mekari.mokaaddons.common.webhook.persistence.storage.EventSourceStorage;

public class DbEventSourceStorage implements EventSourceStorage {

    private final DataSource dataSource;
    private static final String GET_EVENTDATE_SQL = "SELECT event_date FROM event_source WHERE data_id = ? LIMIT 1";
    private static final String INSERT_NEW_ITEM_SQL = "INSERT INTO event_source (data_id, event_date, event_name, payload, event_id, outlet_id, version, timestamp, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

    public DbEventSourceStorage(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<OffsetDateTime> getLastEventDate(String dataId) throws Exception {
        Assert.notNull(dataId, "dataId must not be null");
        try (var conn = dataSource.getConnection()) {
            conn.setAutoCommit(true);
            try (var stmt = conn.prepareStatement(GET_EVENTDATE_SQL)) {
                stmt.setString(1, dataId);
                try (var rs = stmt.executeQuery()) {
                    if (!rs.next())
                        return Optional.empty();
                    var updatedAt = rs.getObject(1, OffsetDateTime.class);
                    return Optional.of(updatedAt);
                }
            }
        }
    }

    @Override
    public void insert(NewEventSource eventSource) throws Exception {
        Assert.notNull(eventSource, "eventSource must not be null");
        try (var conn = dataSource.getConnection()) {
            conn.setAutoCommit(true);
            try (var stmt = conn.prepareStatement(INSERT_NEW_ITEM_SQL)) {
                stmt.setString(1, eventSource.getDataId());
                stmt.setObject(2, eventSource.getEventDate());
                stmt.setString(3, eventSource.getEventName());
                stmt.setString(4, eventSource.getPayload());
                stmt.setString(5, eventSource.getEventId());
                stmt.setString(6, eventSource.getOutletId());
                stmt.setInt(7, eventSource.getVersion());
                stmt.setObject(8, eventSource.getTimestamp());
                stmt.setObject(9, eventSource.getCreatedAt());
                stmt.executeUpdate();
            }
        }
    }
}
