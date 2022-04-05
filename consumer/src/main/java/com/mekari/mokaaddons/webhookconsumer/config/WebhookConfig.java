package com.mekari.mokaaddons.webhookconsumer.config;

import javax.sql.DataSource;

import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.handler.RequestHandler;
import com.mekari.mokaaddons.common.webhook.DeadLetterStorage;
import com.mekari.mokaaddons.common.webhook.EventSourceStorage;
import com.mekari.mokaaddons.common.webhook.LockTrackerStorage;
import com.mekari.mokaaddons.common.webhook.moka.handler.EventDateCompareHandler;
import com.mekari.mokaaddons.common.webhook.moka.handler.EventLockHandler;
import com.mekari.mokaaddons.common.webhook.storage.DbDeadLetterStorage;
import com.mekari.mokaaddons.common.webhook.storage.DbEventSourceStorage;
import com.mekari.mokaaddons.common.webhook.storage.DbLockTrackerStorage;
import com.mekari.mokaaddons.webhookconsumer.service.webhook.mokaItemprocessed.MokaItemProcessedRequest;
import com.mekari.mokaaddons.webhookconsumer.service.webhook.mokaItemreceived.MokaItemReceivedRequest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebhookConfig {
    @Bean
    public DeadLetterStorage deadLetterStorage(@Qualifier("eventstore") DataSource dataSource) {
        return new DbDeadLetterStorage(dataSource);
    }

    @Bean
    public EventSourceStorage eventSourceStorage(@Qualifier("eventstore") DataSource dataSource) {
        return new DbEventSourceStorage(dataSource);
    }

    @Bean
    public LockTrackerStorage lockTrackerStorage(@Qualifier("eventstore") DataSource dataSource) {
        return new DbLockTrackerStorage(dataSource);
    }

    @Bean
    public AbstractVoidRequestHandler<MokaItemReceivedRequest> mokaItemReceivedHandler(
            @Qualifier("eventstore") DataSource dataSource, AbstractVoidRequestHandler<MokaItemReceivedRequest> handler,
            EventSourceStorage eventSourceStorage, LockTrackerStorage lockTrackerStorage) {

        return new EventLockHandler<>(dataSource, lockTrackerStorage,
                new EventDateCompareHandler<>(eventSourceStorage, handler));
    }

    @Bean
    public AbstractVoidRequestHandler<MokaItemProcessedRequest> mokaItemEventProcessedHandler(
            @Qualifier("eventstore") DataSource dataSource,
            AbstractVoidRequestHandler<MokaItemProcessedRequest> handler, EventSourceStorage eventSourceStorage,
            LockTrackerStorage lockTrackerStorage) {

        return new EventLockHandler<>(dataSource, lockTrackerStorage,
                new EventDateCompareHandler<>(eventSourceStorage, handler));
    }
}
