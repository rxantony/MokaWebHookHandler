package com.mekari.mokaaddons.api.config;

import javax.sql.DataSource;

import com.mekari.mokaaddons.api.webhook.event.MokaItemReceivedEvent;
import com.mekari.mokaaddons.api.webhook.event.MokaTransactionReceivedEvent;
import com.mekari.mokaaddons.common.webhook.EventNameClassMap;
import com.mekari.mokaaddons.common.webhook.persistence.storage.DeadLetterStorage;
import com.mekari.mokaaddons.common.webhook.persistence.storage.EventSourceStorage;
import com.mekari.mokaaddons.common.webhook.persistence.storage.db.DbDeadLetterStorage;
import com.mekari.mokaaddons.common.webhook.persistence.storage.db.DbEventSourceStorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebhookConfig {
    private @Autowired @Qualifier("eventstore") DataSource dataSource;

    @Bean
    public EventSourceStorage eventStoreStorage() {
        return new DbEventSourceStorage(dataSource);
    }

    @Bean
    public DeadLetterStorage deadLetterConsumerConfig() {
        return new DbDeadLetterStorage(dataSource);
    }

    @Bean("save.publish.event")
    public EventNameClassMap eventClassMap() {
        return new EventNameClassMap()
                .add("moka.item.added", MokaItemReceivedEvent.class)
                .add("moka.item.updated", MokaItemReceivedEvent.class)
                .add("moka.item.deleted", MokaItemReceivedEvent.class);
    }
}