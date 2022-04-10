package com.mekari.mokaaddons.webhookconsumer.webhook.service.event.sendemail;

import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;

import org.springframework.stereotype.Service;

@Service
public class SendEmailHandler extends AbstractVoidRequestHandler<SendEmailRequest> {

    @Override
    protected void handleInternal(SendEmailRequest request) throws Exception {
        logger.info("send email contains event with id:%s, name:%s to outlet:%s"
            , request.getEvent().getHeader().getEventId()
            , request.getEvent().getHeader().getEventName()
            , request.getEvent().getHeader().getOutletId());
    }
    
}