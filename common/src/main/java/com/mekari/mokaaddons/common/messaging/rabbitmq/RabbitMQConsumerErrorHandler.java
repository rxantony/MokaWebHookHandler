package com.mekari.mokaaddons.common.messaging.rabbitmq;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.common.util.DateUtil;
import com.mekari.mokaaddons.common.webhook.EventHandlingException;
import com.mekari.mokaaddons.common.webhook.moka.AbstractEvent;
import com.mekari.mokaaddons.common.webhook.persistence.storage.DeadLetterStorage;
import com.mekari.mokaaddons.common.webhook.persistence.storage.DeadLetterStorage.NewDeadLetter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

@Component
public class RabbitMQConsumerErrorHandler implements ErrorHandler {
    private ObjectMapper mapper;
    private DeadLetterStorage deadLetterStorage;
    private static final Logger logger = LogManager.getFormatterLogger(RabbitMQConsumerErrorHandler.class);

    public RabbitMQConsumerErrorHandler(@Autowired ObjectMapper mapper,
            @Autowired DeadLetterStorage deadLetterStorage) {
        this.mapper = mapper;
        this.deadLetterStorage = deadLetterStorage;
    }

    @Override
    public void handleError(Throwable t) {
        if (t.getCause() != null) {
            if (t.getCause() instanceof JacksonException) {
                logger.error(t.getCause());
                return;
            }

            if (t.getCause() instanceof EventHandlingException) {
                var ex = (EventHandlingException) t.getCause();
                var ev = (AbstractEvent) ex.getEvent();
                NewDeadLetter deadLetter = null;
                try {
                    var builder = NewDeadLetter.builder()
                            .payload(mapper.writeValueAsString(ev))
                            .reason(ex.getLocalizedMessage())
                            .source(ex.getSource() == null ? "cosumer" : ex.getSource())
                            .createdAt(DateUtil.utcNow());

                    if (ev != null) {
                        builder.eventId(ev.getHeader().getEventId());
                    }

                    deadLetter = builder.build();
                    deadLetterStorage.insert(deadLetter);
                } catch (Exception iex) {
                    logger.error(iex);
                }
                return;
            }
        }
        throw new AmqpRejectAndDontRequeueException(t.getMessage());
    }

}
