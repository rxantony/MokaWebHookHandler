package com.mekari.mokaaddons.common.webhook.moka.handler;

import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.handler.RequestHandler;
import com.mekari.mokaaddons.common.webhook.EventSourceStorage;
import com.mekari.mokaaddons.common.webhook.moka.EventSourceNotFoundException;

import org.springframework.util.Assert;

public class EventDateCompareHandler<TRequest extends EventRequest<?>> extends AbstractVoidRequestHandler<TRequest> {

    private final EventSourceStorage storage;
    private final RequestHandler<TRequest, Void> next;

    public EventDateCompareHandler(EventSourceStorage storage, RequestHandler<TRequest, Void> next) {

        Assert.notNull(storage, "storage must not be null");
        Assert.notNull(next, "next must not be null");
        this.storage = storage;
        this.next = next;
    }

    @Override
    protected void handleInternal(TRequest request) throws Exception {
        var header = request.getEvent().getHeader();
        var body = request.getEvent().getBody();
        
        logger.debug("eventId:%s-eventName:%s-bodyId:%s tries to connect to db for updatedAt date comparing ", 
                header.getEventId(), header.getEventName(), body.getData().getId());
        
        var eventDate = storage.getEventDate(body.getData().getId().toString());
        if(eventDate.isEmpty())
            throw new EventSourceNotFoundException(request.getEvent());

        var isDateEquals = header.getTimestamp().equals(eventDate.get());
        logger.info( "eventId:%s-eventName:%s-bodyId:%s compares updatedAt:%s to eventsource eventDate:%s, result:%b",
                header.getEventId(), header.getEventName(), body.getData().getId(), header.getTimestamp().toString(), eventDate.toString(), isDateEquals);

        if (isDateEquals)
            next.handle(request);
        
    }
}
