package com.mekari.mokaaddons.common.webhook.storage;

import java.time.OffsetDateTime;
import java.util.Optional;

import javax.sql.DataSource;

import com.mekari.mokaaddons.common.webhook.EventSourceStorage;

public class DbEventSourceStorage implements EventSourceStorage {

    private final DataSource dataSource;
    private static final String GET_EVENTDATE_SQL = "SELECT event_date FROM event_source WHERE data_id = ? LIMIT 1";
    private static final String INSERT_NEW_ITEM_SQL = "INSERT INTO event_source (data_id, event_date, event_name, payload, event_id, outlet_id, version, timestamp, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

    public DbEventSourceStorage(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<OffsetDateTime> getLastEventDate(String dataId) throws Exception {
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
    public void insert(NewItem item) throws Exception {
        try (var conn = dataSource.getConnection()) {
            conn.setAutoCommit(true);
            try (var stmt = conn.prepareStatement(INSERT_NEW_ITEM_SQL)) {
                stmt.setString(1, item.getDataId());
                stmt.setObject(2, item.getEventDate());
                stmt.setString(3, item.getEventName());
                stmt.setString(4, item.getPayload());
                stmt.setString(5, item.getEventId());
                stmt.setString(6, item.getOutletId());
                stmt.setInt(7, item.getVersion());
                stmt.setObject(8, item.getTimestamp());
                stmt.setObject(9, item.getCreatedAt());
                stmt.executeUpdate();
            }
        }
    }
}
