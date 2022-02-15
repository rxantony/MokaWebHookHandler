package com.mekari.mokaaddons.webhookhandler.common.storage;

import java.time.OffsetDateTime;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

public class DbLockTrackerStorage implements LockTrackerStorage {

    private JdbcTemplate jdbcTemplate;

    private final static String DELETE_LOCKTRACKER_SQL = "DELETE FROM lock_tracker WHERE connId=? and created_at= ?";
    private final static String INSERT_INTO_LOCKTRACKER_SQL = "INSERT INTO lock_tracker (connId, event_id, data_id, query, created_at) VALUES (?,?,?,?,?);";

    public DbLockTrackerStorage(DataSource dataSource){
        Assert.notNull(dataSource, "dataSource must not be null");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void insert(Item item) throws Exception {
        jdbcTemplate.update(INSERT_INTO_LOCKTRACKER_SQL, item.getConnId(), item.getEventId(), item.getDataId(),
                item.getQuery(), item.getCreatedAt());
    }

    @Override
    public void delete(int connId, OffsetDateTime createdAt) {
        jdbcTemplate.update(DELETE_LOCKTRACKER_SQL, connId, createdAt);
    }

}
