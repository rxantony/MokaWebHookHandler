package com.mekari.mokaaddons.api.config;

import javax.sql.DataSource;

import com.mekari.mokaaddons.api.service.webhook.handleevent.MokaItemReceivedEvent;
import com.mekari.mokaaddons.api.service.webhook.handleevent.MokaTransactionReceivedEvent;
import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.webhook.DeadLetterStorage;
import com.mekari.mokaaddons.common.webhook.EventSourceStorage;
import com.mekari.mokaaddons.common.webhook.moka.service.savethenpublishevent.EventNameClassMap;
import com.mekari.mokaaddons.common.webhook.moka.service.savethenpublishevent.SaveThenPublishEventHandler;
import com.mekari.mokaaddons.common.webhook.moka.service.savethenpublishevent.SaveThenPublishEventRequest;
import com.mekari.mokaaddons.common.webhook.storage.DbDeadLetterStorage;
import com.mekari.mokaaddons.common.webhook.storage.DbEventSourceStorage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public AbstractVoidRequestHandler<SaveThenPublishEventRequest> saveAndPublishHandler() {
        return new SaveThenPublishEventHandler(AppConstant.ExchangeName.MOKA_EVENT_RECEIVED_EXCHANGE);
    }

    @Bean
    public EventNameClassMap eventClassMap() {
        return new EventNameClassMap()
                .add("moka.item.added", MokaItemReceivedEvent.class)
                .add("moka.item.updated", MokaItemReceivedEvent.class)
                .add("moka.item.deleted", MokaItemReceivedEvent.class)
                .add("moka.transaction.created", MokaTransactionReceivedEvent.class)
                .add("moka.transaction.updated", MokaTransactionReceivedEvent.class)
                .add("moka.transaction.refunded", MokaTransactionReceivedEvent.class);
    }
}