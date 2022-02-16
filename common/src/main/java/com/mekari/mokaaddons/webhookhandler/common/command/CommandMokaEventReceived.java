package com.mekari.mokaaddons.webhookhandler.common.command;

import java.time.Instant;
import java.time.ZoneOffset;

import javax.sql.DataSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.webhookhandler.common.event.AbstractMokaEvent;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

public class CommandMokaEventReceived<TEvent extends AbstractMokaEvent<?>> extends AbstractCommand<TEvent> {

    private ObjectMapper mapper;
    private AmqpTemplate amqpTemplate;
    private JdbcTemplate jdbcTemplate;
    private String exchangeName;

    private static final String INSERT_INTO_EVENT_SOURCE_SQL = "INSERT INTO event_source (data_id, data_updated_at, event_name, payload, event_id, outlet_id , version, timestamp, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public CommandMokaEventReceived(Config config
        , Class<TEvent> eventCls) {
        super(eventCls);

        this.exchangeName = config.exchangeName;
        jdbcTemplate = new JdbcTemplate(config.dataSource);
        this.amqpTemplate = config.amqpTemplate;
        this.mapper = config.mapper;
    }

    public void execute(TEvent event) throws CommandException {
        try {
            var header = event.getHeader();
            var data = event.getBody().getData();

            logger.info("start processing eventId:%s, eventName:%s, dataId:%s; updatedAt:%s",
                    header.getEventId(), header.getEventName(), data.getId(),
                    data.getUpdatedAt().toString());

            saveEvent(event);
            publishEvent(event);
        } catch (Exception ex) {
            throw new CommandException(ex);
        }
    }

    protected void saveEvent(TEvent event) throws JsonProcessingException {
        var header = event.getHeader();
        var data = event.getBody().getData();

        logger.info("insert event into even_store eventId:%s, eventName:%s, dataId:%s; updatedAt:%s",
                header.getEventId(), header.getEventName(), data.getId(),
                data.getUpdatedAt().toString());

        jdbcTemplate.update(INSERT_INTO_EVENT_SOURCE_SQL, data.getId(), data.getUpdatedAt(),
                header.getEventName(), mapper.writeValueAsString(event), header.getEventId(),
                header.getOutletId(), header.getVersion(), header.getTimestamp(),
                Instant.now().atOffset(ZoneOffset.UTC));
    }

    protected void publishEvent(TEvent event) {
        var header = event.getHeader();
        var data = event.getBody().getData();

        logger.info(
                "publish webHookEventReceived eventId:%s, eventName:%s, dataId:%s, updatedAt:%s into Queue:%sQueue",
                header.getEventId(), header.getEventName(), data.getId(),
                data.getUpdatedAt().toString(), exchangeName);

        amqpTemplate.convertAndSend(exchangeName, null, event);
    }

    public static class Config{
        public final String exchangeName;
        public final ObjectMapper mapper;
        public final DataSource dataSource;
        public final AmqpTemplate amqpTemplate;

        public Config(String exchangeName, DataSource dataSource, AmqpTemplate amqpTemplate, ObjectMapper mapper){
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