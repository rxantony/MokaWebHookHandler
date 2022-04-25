package com.mekari.mokaaddons.common.messaging.rabbitmq;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.common.util.DateUtil;
import com.mekari.mokaaddons.common.webhook.DeadLetterStorage;
import com.mekari.mokaaddons.common.webhook.EventHandlingException;
import com.mekari.mokaaddons.common.webhook.DeadLetterStorage.NewItem;
import com.mekari.mokaaddons.common.webhook.moka.AbstractEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ErrorHandler;

public class RabbitMQConsumerErrorHandler implements ErrorHandler {
    private @Autowired ObjectMapper mapper;
    private @Autowired DeadLetterStorage deadLetterStorage;
    private static final Logger logger = LogManager.getFormatterLogger(RabbitMQConsumerErrorHandler.class);


    @Override
    public void handleError(Throwable t) {
        if (t.getCause() != null) {
            if(t.getCause() instanceof JacksonException) {
                logger.error(t.getCause());
            } 
            else if(t.getCause() instanceof EventHandlingException){
                var ex = (EventHandlingException) t.getCause();
                var ev = (AbstractEvent) ex.getEvent();
                DeadLetterStorage.NewItem deadLetter = null;
                try{
                    var builder = NewItem.builder()
                    .payload(mapper.writeValueAsString(ev))
                    .reason(ex.getLocalizedMessage())
                    .source(ex.getSource() == null ? "cosumer" : ex.getSource())
                    .createdAt(DateUtil.utcNow());
                    
                    if(ev != null){
                        builder.eventId(ev.getHeader().getEventId());
                    }
                    
                    deadLetter = builder.build();
                    deadLetterStorage.insert(deadLetter);
                }
                catch(Exception iex){
                    logger.error(iex);
                }

            }
            return;
        }
        throw new AmqpRejectAndDontRequeueException(t.getMessage());
    }

}
