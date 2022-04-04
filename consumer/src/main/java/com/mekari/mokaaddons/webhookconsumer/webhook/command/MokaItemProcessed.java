package com.mekari.mokaaddons.webhookconsumer.webhook.command;

import java.util.UUID;

import javax.sql.DataSource;

import com.mekari.mokaaddons.common.webhook.AbstractCommand;
import com.mekari.mokaaddons.common.webhook.CommandException;
import com.mekari.mokaaddons.webhookconsumer.webhook.event.MokaItemProcessedEvent;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class MokaItemProcessed extends AbstractCommand<MokaItemProcessedEvent> {
    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ITEM_SQL = "SELECT jurnal_id FROM item WHERE id=?";
    private static final String UPDATE_ITEM_JURNAL_ID_SQL = "UPDATE item SET jurnal_id=? WHERE id=?";

    public MokaItemProcessed(@Qualifier("mokaaddons") DataSource dataSource) {
        super(MokaItemProcessedEvent.class);

        Assert.notNull(dataSource, "dataSource must not be null");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    protected void executeInternal(MokaItemProcessedEvent event) throws Exception {
        var data = event.getBody().getData();
        var rs = jdbcTemplate.queryForRowSet(SELECT_ITEM_SQL, data.getId());
        if (!rs.next())
            throw new CommandException(String.format("item with id%s is not exists", data.getId()), event);

        var jurnalId = rs.getString("jurnal_id");
        callJurnalApiForCRUD(event, jurnalId);
    }

    private void callJurnalApiForCRUD(MokaItemProcessedEvent event, String jurnalId) {
        var header = event.getHeader();
        var data = event.getBody().getData();

        if (jurnalId != null) {
            // call a jurnal api to update jurnal item
            logger.info(
                    "eventId:%s-eventName:%s-dataId:%s calls jurnal api to update an jurnal item with jurnalId:jurnalId:%s",
                    header.getEventId(), header.getEventName(), data.getId(), jurnalId);
        } else {
            logger.info("eventId:%s-eventName:%s-dataId:%s, call jurnal api to create a new jurnal item",
                    header.getEventId(), header.getEventName(), data.getId());
            // call a jurnal api to create a new jurnal item
            jurnalId = (UUID.randomUUID().toString());

            logger.info("eventId:%s-eventName:%s-dataId:%s update jurnal_id with %s",
                    header.getEventId(), header.getEventName(), data.getId(), jurnalId);
            jdbcTemplate.update(UPDATE_ITEM_JURNAL_ID_SQL, jurnalId, data.getId());

        }
    }
}
