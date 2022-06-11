package com.mekari.mokaaddons.webhookconsumer.event.item.processor.processed;

import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;
import com.mekari.mokaaddons.common.messaging.Publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventItemProcessedHandler extends AbstractVoidRequestHandler<EventItemProcessedRequest> {

    private Publisher publisher;
    private RequestHandlerManager handlerManager;

    public EventItemProcessedHandler(@Autowired RequestHandlerManager handlerManager, @Autowired Publisher publisher){
        this.publisher = publisher;
        this.handlerManager = handlerManager;
    }

    @Override
    protected void handleInternal(EventItemProcessedRequest request) throws Exception {
        logger.debug("create your logic requirements here");
    }

}