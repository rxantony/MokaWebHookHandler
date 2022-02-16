package com.mekari.mokaaddons.webhookhandler.common.consumer;

import com.mekari.mokaaddons.webhookhandler.common.WebHookHandlingException;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.util.ErrorHandler;

public class WebHookHandlingErrorHandler implements ErrorHandler{

    @Override
    public void handleError(Throwable t) {
        if(t.getCause() != null)
            if(t.getCause() instanceof WebHookHandlingException)
                t = t.getCause();
        throw new AmqpRejectAndDontRequeueException(t.getMessage());  
    }
    
}
