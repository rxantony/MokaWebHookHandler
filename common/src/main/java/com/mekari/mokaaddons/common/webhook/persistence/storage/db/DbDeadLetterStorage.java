package com.mekari.mokaaddons.common.webhook.persistence.storage.db;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import com.mekari.mokaaddons.common.webhook.persistence.storage.DeadLetterStorage;

public class DbDeadLetterStorage implements DeadLetterStorage {

    private final JdbcTemplate jdbcTemplate;
    private static final String INSERT_NEW_ITEM_SQL = "INSERT INTO dead_letter(source, event_id, payload, properties, reason, created_at) VALUES (?,?,?,?,?,?)";

    public DbDeadLetterStorage(DataSource dataSource) {
        Assert.notNull(dataSource, "dataSource must not be null");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void insert(NewDeadLetter deadLetter) throws Exception {
        Assert.notNull(deadLetter, "deadLetter must not be null");
        jdbcTemplate.update(INSERT_NEW_ITEM_SQL, deadLetter.getSource(), deadLetter.getEventId(),
                deadLetter.getPayload(),
                deadLetter.getProperties(), deadLetter.getReason(), deadLetter.getCreatedAt());
    }

}
