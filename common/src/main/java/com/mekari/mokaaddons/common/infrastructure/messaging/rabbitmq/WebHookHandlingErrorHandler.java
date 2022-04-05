package com.mekari.mokaaddons.common.infrastructure.messaging.rabbitmq;

import com.fasterxml.jackson.core.JacksonException;
import com.mekari.mokaaddons.common.webhook.moka.EventSourceNotFoundException;

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
            if(t.getCause() instanceof EventSourceNotFoundException)
                return;

        throw new AmqpRejectAndDontRequeueException(t.getMessage());  
    }
    
}
