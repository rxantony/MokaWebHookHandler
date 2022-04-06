package com.mekari.mokaaddons.webhookconsumer.webhook.service.item.processed;

import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;
import com.mekari.mokaaddons.common.infrastructure.messaging.Publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MokaItemProcessedRequestHandler extends AbstractVoidRequestHandler<MokaItemProcessedRequest> {

    private @Autowired Publisher publisher;
    private @Autowired RequestHandlerManager requestManager;

    @Override
    protected void handleInternal(MokaItemProcessedRequest request) throws Exception {
        logger.debug("create your logic requirements here");
    }

}