package com.mekari.mokaaddons.webhookhandler.common.storage;

import java.time.OffsetDateTime;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

public class DbLockTrackerStorage implements LockTrackerStorage {

    private JdbcTemplate jdbcTemplate;

    private final static String DELETE_LOCKTRACKER_SQL = "DELETE FROM lock_tracker WHERE conn_id=? and created_at= ?"; //trx_id trx_query, trx_started,
    private final static String INSERT_INTO_LOCKTRACKER_SQL = "INSERT INTO lock_tracker (conn_id, trx_id, event_id, event_name, data_id, query, trx_started, created_at) SELECT ?, trx_id, ?, ?, ?, ?, trx_started, ? FROM INFORMATION_SCHEMA.INNODB_TRX where trx_mysql_thread_id=?";

    public DbLockTrackerStorage(DataSource dataSource){
        Assert.notNull(dataSource, "dataSource must not be null");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void insert(Item item) throws Exception {
        jdbcTemplate.update(INSERT_INTO_LOCKTRACKER_SQL, item.getConnId(), item.getEventId(), item.getEventName(), item.getDataId(), item.getQuery(), item.getCreatedAt(), item.getConnId());
    }

    @Override
    public void delete(int connId, OffsetDateTime createdAt) {
        jdbcTemplate.update(DELETE_LOCKTRACKER_SQL, connId, createdAt);
    }

}
