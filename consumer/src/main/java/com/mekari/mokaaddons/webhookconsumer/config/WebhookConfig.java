package com.mekari.mokaaddons.webhookconsumer.config;

import javax.sql.DataSource;

import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.webhook.DeadLetterStorage;
import com.mekari.mokaaddons.common.webhook.EventLoggerHandler;
import com.mekari.mokaaddons.common.webhook.EventNameClassMap;
import com.mekari.mokaaddons.common.webhook.EventSourceStorage;
import com.mekari.mokaaddons.common.webhook.LockTrackerStorage;
import com.mekari.mokaaddons.common.webhook.moka.EventDateCompareHandler;
import com.mekari.mokaaddons.common.webhook.moka.EventLockHandler;
import com.mekari.mokaaddons.common.webhook.storage.DbDeadLetterStorage;
import com.mekari.mokaaddons.common.webhook.storage.DbEventSourceStorage;
import com.mekari.mokaaddons.common.webhook.storage.DbLockTrackerStorage;
import com.mekari.mokaaddons.webhookconsumer.webhook.event.MokaItemProcessedEvent;
import com.mekari.mokaaddons.webhookconsumer.webhook.event.MokaItemReceivedEvent;
import com.mekari.mokaaddons.webhookconsumer.webhook.service.event.sendemail.SendEmailRequest;
import com.mekari.mokaaddons.webhookconsumer.webhook.service.item.processed.ProcessMokaItemProcessedRequest;
import com.mekari.mokaaddons.webhookconsumer.webhook.service.item.received.ProcessMokaItemReceivedRequest;

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
                .add("moka.item.added", MokaItemReceivedEvent.class, (e) -> new ProcessMokaItemReceivedRequest(e))
                .add("moka.item.updated", MokaItemReceivedEvent.class, (e) -> new ProcessMokaItemReceivedRequest(e))
                .add("moka.item.deleted", MokaItemReceivedEvent.class, (e) -> new ProcessMokaItemReceivedRequest(e))
                .add("moka.item.processed", MokaItemProcessedEvent.class, (e) -> new ProcessMokaItemProcessedRequest(e));
    }

    @Bean("send.email.event")
    public EventNameClassMap eventClassMapSendEmail() {
        return new EventNameClassMap()
                .add("moka.item.added", MokaItemReceivedEvent.class, (e) -> new SendEmailRequest(e))
                .add("moka.item.updated", MokaItemReceivedEvent.class, (e) -> new SendEmailRequest(e))
                .add("moka.item.deleted", MokaItemReceivedEvent.class, (e) -> new SendEmailRequest(e))
                .add("moka.item.processed", MokaItemProcessedEvent.class, (e) -> new SendEmailRequest(e));
    }

    @Bean
    public AbstractVoidRequestHandler<ProcessMokaItemReceivedRequest> mokaItemReceivedRequestHandler(
            AbstractVoidRequestHandler<ProcessMokaItemReceivedRequest> handler) {
        // return new EventLockHandler<>(dataSource, lockTrackerStorage,
        // new EventDateCompareHandler<>(eventSourceStorage, handler));

        return new EventLoggerHandler<>(
                new EventDateCompareHandler<>(eventSourceStorage(), handler));
    }

    @Bean
    public AbstractVoidRequestHandler<ProcessMokaItemProcessedRequest> mokaItemProcessedRequestHandler(
            AbstractVoidRequestHandler<ProcessMokaItemProcessedRequest> handler) {
        return new EventLockHandler<>(dataSource, lockTrackerStorage(),
                new EventDateCompareHandler<>(eventSourceStorage(), handler));
    }
}
