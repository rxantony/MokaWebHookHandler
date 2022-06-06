package com.mekari.mokaaddons.webhookconsumer.webhook.service.item.command.handleprocessed;

import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;
import com.mekari.mokaaddons.common.messaging.Publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MokaItemProcessedHandler extends AbstractVoidRequestHandler<HandleMokaItemProcessedRequest> {

    private @Autowired Publisher publisher;
    private @Autowired RequestHandlerManager handlerManager;

    @Override
    protected void handleInternal(HandleMokaItemProcessedRequest request) throws Exception {
        logger.debug("create your logic requirements here");
    }

}