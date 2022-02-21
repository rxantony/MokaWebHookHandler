package com.mekari.mokaaddons.webhookhandler.common.storage;

import java.time.OffsetDateTime;
import java.util.Optional;

import javax.sql.DataSource;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;

import org.springframework.util.Assert;

public class DbEventSourceStorage implements EventSourceStorage {
    private final DataSource dataSource;
    private static final String GET_UPDATEDAT_SQL = "SELECT data_updated_at FROM event_source WHERE data_id = ? LIMIT 1";

    public DbEventSourceStorage(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<OffsetDateTime> getEventDate(Event event) throws Exception {
        Assert.notNull(event, "event must not be null");
        Assert.notNull(event.getBody(), "event.getBody() must not be null");
        Assert.notNull(event.getBody().getData(), "event.getBody().getData() must not be null");
        
        try (var conn = dataSource.getConnection()) {
            conn.setAutoCommit(true);
            try (var stmt = conn.prepareStatement(GET_UPDATEDAT_SQL)) {
                stmt.setString(1, event.getBody().getData().getId());
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
