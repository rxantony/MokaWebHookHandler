package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;
import com.mekari.mokaaddons.webhookhandler.common.storage.EventSourceStorage;

import org.springframework.util.Assert;

public class EventDateCompareCommand<TEvent extends Event> extends AbstractEventCommand<TEvent> {

    private final EventSourceStorage storage;
    private final EventCommand<TEvent> inner;

    public EventDateCompareCommand(EventSourceStorage storage, EventCommand<TEvent> inner) {
        super(inner.eventClass());

        Assert.notNull(storage, "storage must not be null");
        Assert.notNull(inner, "inner must not be null");
        this.storage = storage;
        this.inner = inner;
    }

    @Override
    protected void executeInternal(TEvent event) throws Exception {
        logger.debug("eventId:%s-eventName:%s-bodyId:%s tries to connect to db for updatedAt date comparing ", 
                event.geId(), event.getName(), event.getBody().getId());
        
        var eventDate = storage.getEventDate(event);
        if(eventDate.isEmpty())
            throw new EventSourceDataNotFoundException(event);

        var isDateEquals = event.getDate().equals(eventDate.get());
        logger.info( "eventId:%s-eventName:%s-bodyId:%s compares updatedAt:%s to eventsource eventDate:%s, result:%b",
                event.geId(), event.getName(), event.getBody().getId(), event.getDate().toString(), eventDate.toString(), isDateEquals);

        if (isDateEquals)
            inner.execute(event);
    }
}
