package com.mekari.mokaaddons.webhookconsumer.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mekari.mokaaddons.common.webhook.event.EventNameClassMap;
import com.mekari.mokaaddons.common.webhook.persistence.storage.*;
import com.mekari.mokaaddons.common.webhook.persistence.storage.db.*;
import com.mekari.mokaaddons.webhookconsumer.event.item.MokaItemProcessedEvent;
import com.mekari.mokaaddons.webhookconsumer.event.item.MokaItemReceivedEvent;
import com.mekari.mokaaddons.webhookconsumer.event.item.processor.processed.HandleItemProcessedEventRequest;
import com.mekari.mokaaddons.webhookconsumer.event.item.processor.received.HandleItemReceivedEventRequest;

@Configuration
public class WebhookConfig {

    private @Autowired @Qualifier("eventstore") DataSource dataSource;

    @Bean
    public DeadLetterStorage deadLetterStorage() {
        return new DbDeadLetterStorage(dataSource);
    }

    @Bean
    public EventSourceStorage eventSourceStorage() {
        return new DbEventSourceStorage(dataSource);
    }

    @Bean
    public LockTrackerStorage lockTrackerStorage() {
        return new DbLockTrackerStorage(dataSource);
    }

    @Bean("moka.item.event")
    public EventNameClassMap eventClassMapSavePublish() {
        return new EventNameClassMap()
                .add("moka.item.added", MokaItemReceivedEvent.class, HandleItemReceivedEventRequest::new)
                .add("moka.item.updated", MokaItemReceivedEvent.class, HandleItemReceivedEventRequest::new)
                .add("moka.item.deleted", MokaItemReceivedEvent.class, HandleItemReceivedEventRequest::new)
                .add("moka.item.processed", MokaItemProcessedEvent.class, HandleItemProcessedEventRequest::new);
    }
}
