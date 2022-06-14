package com.mekari.mokaaddons.webhookconsumer.event.item.processor.processed;

import org.springframework.stereotype.Service;

import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;

@Service
public class EventItemProcessedHandler extends AbstractVoidRequestHandler<EventItemProcessedRequest> {

    @Override
    protected void handleInternal(EventItemProcessedRequest request) throws Exception {
        logger.debug("create your logic requirements here");
    }
}