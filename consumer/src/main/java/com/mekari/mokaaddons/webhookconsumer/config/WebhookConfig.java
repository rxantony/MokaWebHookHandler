package com.mekari.mokaaddons.webhookconsumer.config;

import javax.sql.DataSource;

import com.mekari.mokaaddons.common.webhook.DeadLetterStorage;
import com.mekari.mokaaddons.common.webhook.EventNameClassMap;
import com.mekari.mokaaddons.common.webhook.EventSourceStorage;
import com.mekari.mokaaddons.common.webhook.LockTrackerStorage;
import com.mekari.mokaaddons.common.webhook.storage.DbDeadLetterStorage;
import com.mekari.mokaaddons.common.webhook.storage.DbEventSourceStorage;
import com.mekari.mokaaddons.common.webhook.storage.DbLockTrackerStorage;
import com.mekari.mokaaddons.webhookconsumer.webhook.service.item.command.handleprocessed.HandleMokaItemProcessedRequest;
import com.mekari.mokaaddons.webhookconsumer.webhook.service.item.command.handlereceived.HandleMokaItemReceivedRequest;
import com.mekari.mokaaddons.webhookconsumer.webhook.service.item.event.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    @Bean("save.publish.event")
    public EventNameClassMap eventClassMapSavePublish() {
        return new EventNameClassMap()
                .add("moka.item.added", MokaItemReceivedEvent.class, HandleMokaItemReceivedRequest::new)
                .add("moka.item.updated", MokaItemReceivedEvent.class, HandleMokaItemReceivedRequest::new)
                .add("moka.item.deleted", MokaItemReceivedEvent.class, HandleMokaItemReceivedRequest::new)
                .add("moka.item.processed", MokaItemProcessedEvent.class, HandleMokaItemProcessedRequest::new);
    }
}
