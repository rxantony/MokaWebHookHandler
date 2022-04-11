package com.mekari.mokaaddons.webhookconsumer.webhook.service.item.processed;

import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;
import com.mekari.mokaaddons.common.infrastructure.messaging.Publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessMokaItemProcessedHandler extends AbstractVoidRequestHandler<ProcessMokaItemProcessedRequest> {

    private @Autowired Publisher publisher;
    private @Autowired RequestHandlerManager handlerManager;

    @Override
    protected void handleInternal(ProcessMokaItemProcessedRequest request) throws Exception {
        logger.debug("create your logic requirements here");
    }

}