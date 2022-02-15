package com.mekari.mokaaddons.webhookhandler.common.processor;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;
import com.mekari.mokaaddons.webhookhandler.common.storage.EventSourceStorage;

import org.springframework.util.Assert;

public class EventUpdateAtValidationProcessor<TEvent extends Event> extends AbstractEventProcessor<TEvent> {

    private final EventSourceStorage storage;
    private final EventProcessor<TEvent> innerProcessor;

    public EventUpdateAtValidationProcessor(EventSourceStorage storage, EventProcessor<TEvent> innerProcessor) {
        super(innerProcessor.eventClass());

        Assert.notNull(storage, "storage must not be null");
        this.storage = storage;
        this.innerProcessor = innerProcessor;
    }

    @Override
    public void process(TEvent event) throws EventProcessingException {
        Assert.notNull(event, "event must not be null");

        var header = event.getHeader();
        var data = event.getBody().getData();

        logger.debug("eventId:%s-dataId:%s tries to connect to db for updatedAt date comparing ", header.getEventId(),
                data.getId());

        try{
            var dataUpdatedAt = storage.getUpdateAt(data.getId());
            if(dataUpdatedAt.isEmpty())
                throw new EventSourceDataNotFoundException(data.getId());

            var isUpdatedAtEquals = data.getUpdatedAt().equals(dataUpdatedAt.get());
            logger.info(
                    "eventId:%s-dataId:%s compares updatedAt:%s to eventsource updatedAt:%s, result:%b",
                    header.getEventId(), data.getId(),
                    data.getUpdatedAt().toString(), dataUpdatedAt.toString(), isUpdatedAtEquals);

            if (isUpdatedAtEquals)
                innerProcessor.process(event);

        } catch (Exception ex) {
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
