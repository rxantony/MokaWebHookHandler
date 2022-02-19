package com.mekari.mokaaddons.webhookhandler.common.command;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.webhookhandler.common.event.AbstractMokaEvent;
import com.mekari.mokaaddons.webhookhandler.common.util.DateUtil;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.util.Assert;

public class CommandMokaEventReceived<TEvent extends AbstractMokaEvent<?>> extends AbstractCommandEvent<TEvent> {

    private Config config;

    private static final String INSERT_INTO_EVENT_SOURCE_SQL = "INSERT INTO event_source (data_id, data_updated_at, event_name, payload, event_id, outlet_id , version, timestamp, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public CommandMokaEventReceived(Config config, Class<TEvent> eventCls) {
        super(eventCls);
        Assert.notNull(config, "config must not be null");
        this.config = config;
    }

    protected void executeInternal(TEvent event) throws Exception {
        saveEvent(event);
        publishEvent(event);
    }

    protected void saveEvent(TEvent event) throws Exception {
        var header = event.getHeader();
        var data = event.getBody().getData();

        logger.info("insert event into even_store eventId:%s-eventName:%s-dataId:%s with updatedAt:%s",
                header.getEventId(), header.getEventName(), data.getId(),
                data.getUpdatedAt().toString());

        try (var conn = config.dataSource.getConnection()) {
            conn.setAutoCommit(true);
            try (var stmt = conn.prepareStatement(INSERT_INTO_EVENT_SOURCE_SQL)) {
                stmt.setString(1, data.getId());
                stmt.setObject(2, data.getUpdatedAt());
                stmt.setString(3, header.getEventName());
                stmt.setString(4, config.mapper.writeValueAsString(event));
                stmt.setString(5, header.getEventId());
                stmt.setString(6, header.getOutletId());
                stmt.setInt(7, header.getVersion());
                stmt.setObject(8, header.getTimestamp());
                stmt.setObject(9, DateUtil.now());
                stmt.executeUpdate();
            }
        }
        /*jdbcTemplate.update(INSERT_INTO_EVENT_SOURCE_SQL, data.getId(), data.getUpdatedAt(),
                header.getEventName(), mapper.writeValueAsString(event), header.getEventId(),
                header.getOutletId(), header.getVersion(), header.getTimestamp(),
                Instant.now().atOffset(ZoneOffset.UTC));*/

    }

    protected void publishEvent(TEvent event) {
        var header = event.getHeader();
        var data = event.getBody().getData();

        logger.info(
                "publish webHookEventReceived eventId:%s-eventName:%s-dataId:%s with updatedAt:%s into Queue:%sQueue",
                header.getEventId(), header.getEventName(), data.getId(), data.getUpdatedAt().toString(), config.exchangeName);

        config.amqpTemplate.convertAndSend(config.exchangeName, null, event);
    }

    public static class Config {
        public final String exchangeName;
        public final ObjectMapper mapper;
        public final DataSource dataSource;
        public final AmqpTemplate amqpTemplate;

        public Config(String exchangeName, DataSource dataSource, AmqpTemplate amqpTemplate, ObjectMapper mapper) {
            Assert.notNull(exchangeName, "exchangeName must not be null");
            Assert.isTrue(exchangeName.trim().length() != 0, "exchangeName must not be empty");
            Assert.notNull(dataSource, "dataSource must not be null");
            Assert.notNull(amqpTemplate, "amqpTemplate must not be null");
            Assert.notNull(mapper, "mapper must not be null");

            this.exchangeName = exchangeName.trim();
            this.dataSource = dataSource;
            this.amqpTemplate = amqpTemplate;
            this.mapper = mapper;
        }
    }
}