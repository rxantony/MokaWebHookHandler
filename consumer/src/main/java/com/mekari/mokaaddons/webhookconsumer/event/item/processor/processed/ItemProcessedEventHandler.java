package com.mekari.mokaaddons.webhookconsumer.event.item.processor.processed;

import org.springframework.stereotype.Service;

import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;

@Service
public class ItemProcessedEventHandler extends AbstractVoidRequestHandler<HandleItemProcessedEventRequest> {

    @Override
    protected void handleInternal(HandleItemProcessedEventRequest request) throws Exception {
        logger.debug("create your logic requirements here");
    }
}