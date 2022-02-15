package com.mekari.mokaaddons.webhookhandler.common.processor;

import java.sql.SQLException;
import java.time.OffsetDateTime;

import javax.sql.DataSource;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;

import org.springframework.util.Assert;

public class DbEventUpdateAtValidationProcessor<TEvent extends Event> extends AbstractEventProcessor<TEvent> {

    private final DataSource dataSource;

    private final EventProcessor<TEvent> innerProcessor;

    private static final String GET_TIMESTAMP_SQL = "SELECT data_updated_at FROM event_source WHERE data_id = ? LIMIT 1";

    public DbEventUpdateAtValidationProcessor(DataSource dataSource, EventProcessor<TEvent> innerProcessor) {
        super(innerProcessor.eventClass());

        Assert.notNull(dataSource, "dataSource must not be null");
        this.dataSource = dataSource;
        this.innerProcessor = innerProcessor;
    }

    @Override
    public void process(TEvent event) throws EventProcessingException {
        Assert.notNull(event, "event must not be null");

        var header = event.getHeader();
        var data = event.getBody().getData();

        logger.debug("eventId:%s-dataId:%s tries to connect to db for updatedAt date comparing ", header.getEventId(),
                data.getId());

        try (var conn = dataSource.getConnection();) {
            var stmt = conn.prepareStatement(GET_TIMESTAMP_SQL);
            stmt.setString(1, data.getId());
            var rs = stmt.executeQuery();
            if (!rs.next())
                throw new EventProcessingException(data.getId());

            var dataUpdatedAt = rs.getObject(1, OffsetDateTime.class);
            var isUpdatedAtEquals = data.getUpdatedAt().equals(dataUpdatedAt);

            logger.info(
                    "eventId:%s-dataId:%s compares updatedAt:%s to eventsource updatedAt:%s, result:%b",
                    header.getEventId(), data.getId(),
                    data.getUpdatedAt().toString(), dataUpdatedAt.toString(), isUpdatedAtEquals);

            if (isUpdatedAtEquals)
                innerProcessor.process(event);

        } catch (SQLException ex) {
            throw new EventProcessingException(ex);
        }
    }

    public static class EventSourceDataNotFoundException extends EventProcessingException {
        private final String dataId;

        public EventSourceDataNotFoundException(String dataId) {
            super(String.format("dataId:%s is not found on event_source", dataId));
            this.dataId = dataId;
        }

        public String getDataId() {
            return dataId;
        }
    }

}
