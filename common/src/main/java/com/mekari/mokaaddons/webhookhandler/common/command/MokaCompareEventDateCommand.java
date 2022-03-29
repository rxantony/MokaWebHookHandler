package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.event.moka.AbstractMokaEvent;
import com.mekari.mokaaddons.webhookhandler.common.storage.EventSourceStorage;

import org.springframework.util.Assert;

public class MokaCompareEventDateCommand<TEvent extends AbstractMokaEvent> extends AbstractCommand<TEvent> {

    private final EventSourceStorage storage;
    private final Command<TEvent> inner;

    public MokaCompareEventDateCommand(EventSourceStorage storage, Command<TEvent> inner) {
        super(inner.eventClass());

        Assert.notNull(storage, "storage must not be null");
        Assert.notNull(inner, "inner must not be null");
        this.storage = storage;
        this.inner = inner;
    }

    @Override
    protected void executeInternal(TEvent event) throws Exception {
        var header = event.getHeader();
        var body = event.getBody();
        
        logger.debug("eventId:%s-eventName:%s-bodyId:%s tries to connect to db for updatedAt date comparing ", 
                header.getEventId(), header.getEventName(), body.getData().getId());
        
        var eventDate = storage.getEventDate(body.getData().getId().toString());
        if(eventDate.isEmpty())
            throw new EventSourceNotFoundException(event);

        var isDateEquals = header.getTimestamp().equals(eventDate.get());
        logger.info( "eventId:%s-eventName:%s-bodyId:%s compares updatedAt:%s to eventsource eventDate:%s, result:%b",
                header.getEventId(), header.getEventName(), body.getData().getId(), header.getTimestamp().toString(), eventDate.toString(), isDateEquals);

        if (isDateEquals)
            inner.execute(event);
    }
}
