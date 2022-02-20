package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;
import com.mekari.mokaaddons.webhookhandler.common.storage.EventSourceStorage;

import org.springframework.util.Assert;

public class CommandEventDateCompare<TEvent extends Event> extends AbstractCommandEvent<TEvent> {

    private final EventSourceStorage storage;
    private final CommandEvent<TEvent> inner;

    public CommandEventDateCompare(EventSourceStorage storage, CommandEvent<TEvent> inner) {
        super(inner.eventClass());

        Assert.notNull(storage, "storage must not be null");
        Assert.notNull(inner, "inner must not be null");
        
        this.storage = storage;
        this.inner = inner;
    }

    @Override
    protected void executeInternal(TEvent event) throws Exception {
        Assert.notNull(event, "event must not be null");

        var header = event.getHeader();
        var data = event.getBody().getData();

        logger.debug("eventId:%s-eventName:%s-dataId:%s tries to connect to db for updatedAt date comparing ", 
                header.getEventId(), header.getEventName(), data.getId());

        var dataUpdatedAt = storage.getUpdateAt(data.getId());
        if(dataUpdatedAt.isEmpty())
            throw new EventSourceDataNotFoundException(event);

        var isUpdatedAtEquals = data.getUpdatedAt().equals(dataUpdatedAt.get());
        logger.info( "eventId:%s-eventName:%s-dataId:%s compares updatedAt:%s to eventsource updatedAt:%s, result:%b",
                header.getEventId(), header.getEventName(), data.getId(), data.getUpdatedAt().toString(), dataUpdatedAt.toString(), isUpdatedAtEquals);

        if (isUpdatedAtEquals)
            inner.execute(event);
    }
}
