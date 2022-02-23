package com.mekari.mokaaddons.webhookhandler.command;

import javax.sql.DataSource;

import com.mekari.mokaaddons.webhookhandler.common.command.AbstractEventCommand;
import com.mekari.mokaaddons.webhookhandler.common.event.EventHeader;
import com.mekari.mokaaddons.webhookhandler.common.event.moka.MokaEventHeader;
import com.mekari.mokaaddons.webhookhandler.common.util.DateUtil;
import com.mekari.mokaaddons.webhookhandler.config.AppConstant;
import com.mekari.mokaaddons.webhookhandler.event.MokaItemProcessed;
import com.mekari.mokaaddons.webhookhandler.event.MokaItemProcessed.*;
import com.mekari.mokaaddons.webhookhandler.event.MokaItemReceived;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class CommandMokaItemReceived extends AbstractEventCommand<MokaItemReceived> {

    private final JdbcTemplate jdbcTemplate;
    private final AmqpTemplate amqpTemplate;

    private static final String IS_ITEM_EXISTS_SQL = "SELECT id FROM item WHERE id=?";
    private static final String INSERT_ITEM_SQL = "INSERT INTO item (id, name, description, created_at, updated_at) VALUES (?,?,?,?,?)";
    private static final String UPDATE_ITEM_SQL = "UPDATE item SET name=?, description=?, updated_at=? WHERE id=?";

    public CommandMokaItemReceived(@Qualifier("mokaaddons") DataSource dataSource,
            @Autowired AmqpTemplate amqpTemplate) {
        super(MokaItemReceived.class);

        Assert.notNull(dataSource, "dataSource must not be null");
        Assert.notNull(amqpTemplate, "amqpTemplate must not be null");

        jdbcTemplate = new JdbcTemplate(dataSource);
        this.amqpTemplate = amqpTemplate;
    }

    @Override
    protected void executeInternal(MokaItemReceived event) throws Exception {
        saveEvent(event);
        publishEvent(event);
    }

    private void saveEvent(MokaItemReceived event) {
        var header = event.getHeader();
        var data = event.getBody().getData();
        var rs = jdbcTemplate.queryForRowSet(IS_ITEM_EXISTS_SQL, data.getId());
        if (!rs.next()) {
            logger.info("eventId:%s-eventName:%s-dataId:%s inserts a new moka item",
                    header.getEventId(), header.getEventName(), data.getId());
            jdbcTemplate.update(INSERT_ITEM_SQL, data.getId(), data.getName(), data.getDescription(),
                    DateUtil.now(), DateUtil.now());
        } else {
            logger.info("eventId:%s-eventName:%s-dataId:%s updates a moka item",
                    header.getEventId(), header.getEventName(), data.getId());
            jdbcTemplate.update(UPDATE_ITEM_SQL, data.getName(), data.getDescription(), DateUtil.now(),
                    data.getId());
        }
    }

    private void publishEvent(MokaItemReceived event) {
        var header = event.getHeader();
        var data = event.getBody().getData();
        
        logger.info(
                "eventId:%s-eventName:%s-dataId:%s publishes webHookEventProcessedcwith  moka.item.processed into Queue:%sQueue",
                header.getEventId(), header.getEventName(), data.getId(),
                AppConstant.ExchangeName.MOKA_EVENT_PROCESSED_EXCHANGE);

        var nextEventHeader = new EventHeader(header.getEventId(), "moka.item.processed", header.getTimestamp());
        var nextEventBody = new Body(new Item(event.getBody().getId()));
        var nextEvent = new MokaItemProcessed(nextEventHeader,nextEventBody);

        amqpTemplate.convertAndSend(AppConstant.ExchangeName.MOKA_EVENT_PROCESSED_EXCHANGE, null, nextEvent);
    }
}
