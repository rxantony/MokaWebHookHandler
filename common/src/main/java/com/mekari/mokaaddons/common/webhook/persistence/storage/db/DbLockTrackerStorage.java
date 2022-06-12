package com.mekari.mokaaddons.common.webhook.persistence.storage.db;

import java.time.OffsetDateTime;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import com.mekari.mokaaddons.common.webhook.persistence.storage.LockTrackerStorage;

public class DbLockTrackerStorage implements LockTrackerStorage {

    private JdbcTemplate jdbcTemplate;
    private static final String DELETE_ITEM_SQL = "DELETE FROM lock_tracker WHERE conn_id=? and created_at= ?";
    private static final String INSERT_NEW_ITEM_SQL = "INSERT INTO lock_tracker (conn_id, trx_id, event_id, event_name, data_id, query, trx_started, created_at) SELECT ?, trx_id, ?, ?, ?, ?, trx_started, ? FROM INFORMATION_SCHEMA.INNODB_TRX where trx_mysql_thread_id=?";

    public DbLockTrackerStorage(DataSource dataSource) {
        Assert.notNull(dataSource, "dataSource must not be null");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void insert(NewLockTracker lockTracker) throws Exception {
        Assert.notNull(lockTracker, "lockTracker must not be null");
        jdbcTemplate.update(INSERT_NEW_ITEM_SQL, lockTracker.getConnId(), lockTracker.getEventId(),
                lockTracker.getEventName(),
                lockTracker.getDataId(), lockTracker.getQuery(), lockTracker.getCreatedAt(), lockTracker.getConnId());
    }

    @Override
    public void delete(int connId, OffsetDateTime createdAt) {
        Assert.notNull(createdAt, "createdAt must not be null");
        jdbcTemplate.update(DELETE_ITEM_SQL, connId, createdAt);
    }

}
