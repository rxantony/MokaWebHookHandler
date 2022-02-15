package com.mekari.mokaaddons.webhookhandler.common.storage;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

public class DbDeadLetterStorage implements DeadLetterStorage {

    private final JdbcTemplate jdbcTemplate;
    
    private static final String INSERT_INTO_DEADLETTER_SQL = "INSERT INTO dead_letter(source, event_id, pay_load, properties, created_at) VALUES (?,?,?,?,?)";

    public DbDeadLetterStorage (DataSource dataSource){
        Assert.notNull(dataSource, "dataSource must not be null");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void insert(DeadLeterItem item) throws Exception {
        Assert.notNull(item, "item must not be null");
        jdbcTemplate.update(INSERT_INTO_DEADLETTER_SQL, item.getSource(), item.getEventId(), item.getPayload(), item.getProperties(), item.getCreatedAt());
    }
    
}
