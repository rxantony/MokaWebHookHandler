package com.mekari.mokaaddons.api.config;

import javax.sql.DataSource;

import com.mekari.mokaaddons.api.service.webhook.handleEvent.MokaItemReceivedEvent;
import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.handler.RequestHandler;
import com.mekari.mokaaddons.common.webhook.DeadLetterStorage;
import com.mekari.mokaaddons.common.webhook.EventNameClassMap;
import com.mekari.mokaaddons.common.webhook.EventSourceStorage;
import com.mekari.mokaaddons.common.webhook.moka.handler.saveAndPublishEvent.SaveAndPublishEventHandler;
import com.mekari.mokaaddons.common.webhook.moka.handler.saveAndPublishEvent.SaveAndPublishEventRequest;
import com.mekari.mokaaddons.common.webhook.storage.DbDeadLetterStorage;
import com.mekari.mokaaddons.common.webhook.storage.DbEventSourceStorage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class WebhookConfig {
    @Bean
    public EventSourceStorage eventStoreStorage(DataSource dataSource) {
        return new DbEventSourceStorage(dataSource);
    }

    @Bean
    public DeadLetterStorage deadLetterConsumerConfig(DataSource dataSource) {
        return new DbDeadLetterStorage(dataSource);
    }

    @Bean
    public AbstractVoidRequestHandler<SaveAndPublishEventRequest> saveAndPublishHandler() {
        return new SaveAndPublishEventHandler(AppConstant.ExchangeName.MOKA_EVENT_RECEIVED_EXCHANGE);
    }

    @Bean
    @Scope("singleton")
    public EventNameClassMap eventClassMap() {
        return new EventNameClassMap()
                .add("moka.item.added", MokaItemReceivedEvent.class)
                .add("moka.item.updated", MokaItemReceivedEvent.class)
                .add("moka.item.deleted", MokaItemReceivedEvent.class);
    }
}