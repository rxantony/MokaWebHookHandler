package com.mekari.mokaaddons.common.consumer;

import com.mekari.mokaaddons.common.webhook.WebhookHandlingException;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.util.ErrorHandler;

public class WebHookHandlingErrorHandler implements ErrorHandler{

    @Override
    public void handleError(Throwable t) {
        if(t.getCause() != null)
            if(t.getCause() instanceof WebhookHandlingException)
                t = t.getCause();
        throw new AmqpRejectAndDontRequeueException(t.getMessage());  
    }
    
}
