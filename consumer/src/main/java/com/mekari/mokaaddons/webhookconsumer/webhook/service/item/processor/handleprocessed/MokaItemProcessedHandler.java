package com.mekari.mokaaddons.webhookconsumer.webhook.service.item.processor.handleprocessed;

import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;
import com.mekari.mokaaddons.common.messaging.Publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MokaItemProcessedHandler extends AbstractVoidRequestHandler<HandleMokaItemProcessedRequest> {

    private Publisher publisher;
    private RequestHandlerManager handlerManager;

    public MokaItemProcessedHandler(@Autowired RequestHandlerManager handlerManager, @Autowired Publisher publisher){
        this.publisher = publisher;
        this.handlerManager = handlerManager;
    }

    @Override
    protected void handleInternal(HandleMokaItemProcessedRequest request) throws Exception {
        logger.debug("create your logic requirements here");
    }

}