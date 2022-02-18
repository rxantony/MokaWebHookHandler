package com.mekari.mokaaddons.webhookhandler.common.storage;

import java.time.OffsetDateTime;
import java.util.Optional;

import javax.sql.DataSource;

public class DbEventSourceStorage implements EventSourceStorage {
    private final DataSource dataSource;
    private static final String GET_UPDATEDAT_SQL = "SELECT data_updated_at FROM event_source WHERE data_id = ? LIMIT 1";

    public DbEventSourceStorage(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<OffsetDateTime> getUpdateAt(String id) throws Exception {
        try (var conn = dataSource.getConnection()) {
            conn.setAutoCommit(true);
            try (var stmt = conn.prepareStatement(GET_UPDATEDAT_SQL)) {
                stmt.setString(1, id);
                try (var rs = stmt.executeQuery()) {
                    if (!rs.next())
                        return Optional.empty();
                    var updatedAt = rs.getObject(1, OffsetDateTime.class);
                    return Optional.of(updatedAt);
                }
            }
        }
    }
}
