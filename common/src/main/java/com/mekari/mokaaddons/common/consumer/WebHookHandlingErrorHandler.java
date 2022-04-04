package com.mekari.mokaaddons.common.consumer;

import com.fasterxml.jackson.core.JacksonException;
import com.mekari.mokaaddons.common.webhook.moka.MokaEventSourceNotFoundException;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.util.ErrorHandler;

public class WebHookHandlingErrorHandler implements ErrorHandler{

    @Override
    public void handleError(Throwable t) {
        if(t.getCause() != null)
            /**
             * if exception is instance of JacksonException, don't reject and requeue it
             */
            if(t.getCause() instanceof JacksonException)
                return;
            if(t.getCause() instanceof MokaEventSourceNotFoundException)
                return;

        throw new AmqpRejectAndDontRequeueException(t.getMessage());  
    }
    
}
