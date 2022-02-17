package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;
import com.mekari.mokaaddons.webhookhandler.common.storage.EventSourceStorage;

import org.springframework.util.Assert;

public class CommandEventUpdateAtValidation<TEvent extends Event> extends AbstractCommandEvent<TEvent> {

    private final EventSourceStorage storage;
    private final CommandEvent<TEvent> inner;

    public CommandEventUpdateAtValidation(EventSourceStorage storage, CommandEvent<TEvent> inner) {
        super(inner.eventClass());

        Assert.notNull(storage, "storage must not be null");
        this.storage = storage;
        this.inner = inner;
    }

    @Override
    protected void executeInternal(TEvent event) throws Exception {
        Assert.notNull(event, "event must not be null");

        var header = event.getHeader();
        var data = event.getBody().getData();

        logger.debug("eventId:%s-dataId:%s tries to connect to db for updatedAt date comparing ", header.getEventId(),
                data.getId());

        var dataUpdatedAt = storage.getUpdateAt(data.getId());
        if(dataUpdatedAt.isEmpty())
            throw new EventSourceDataNotFoundException(data.getId());

        var isUpdatedAtEquals = data.getUpdatedAt().equals(dataUpdatedAt.get());
        logger.info(
                "eventId:%s-dataId:%s compares updatedAt:%s to eventsource updatedAt:%s, result:%b",
                header.getEventId(), data.getId(),
                data.getUpdatedAt().toString(), dataUpdatedAt.toString(), isUpdatedAtEquals);

        if (isUpdatedAtEquals)
            inner.execute(event);
    }

    public static class EventSourceDataNotFoundException extends CommandException {
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
