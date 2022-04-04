package com.mekari.mokaaddons.webhookconsumer.config;

import javax.sql.DataSource;

import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.handler.RequestHandler;
import com.mekari.mokaaddons.common.webhook.DeadLetterStorage;
import com.mekari.mokaaddons.common.webhook.EventSourceStorage;
import com.mekari.mokaaddons.common.webhook.LockTrackerStorage;
import com.mekari.mokaaddons.common.webhook.moka.handler.MokaCompareEventDate;
import com.mekari.mokaaddons.common.webhook.moka.handler.MokaEventLock;
import com.mekari.mokaaddons.common.webhook.storage.DbDeadLetterStorage;
import com.mekari.mokaaddons.common.webhook.storage.DbEventSourceStorage;
import com.mekari.mokaaddons.common.webhook.storage.DbLockTrackerStorage;
import com.mekari.mokaaddons.webhookconsumer.service.webhook.mokaItemProcessed.MokaItemProcessedRequest;
import com.mekari.mokaaddons.webhookconsumer.service.webhook.mokaItemReceived.MokaItemReceivedRequest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebhookConfig {
    @Bean
    public DeadLetterStorage deadLetterStorage(@Qualifier("eventstore") DataSource dataSource){
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
    public AbstractVoidRequestHandler<MokaItemReceivedRequest> mokaItemReceivedRequestHandler(@Qualifier("eventstore") DataSource dataSource
        , AbstractVoidRequestHandler<MokaItemReceivedRequest> handler
        , EventSourceStorage eventSourceStorage
        , LockTrackerStorage lockTrackerStorage) {

        return new MokaEventLock<>(dataSource, lockTrackerStorage,
                    new MokaCompareEventDate<>(eventSourceStorage, handler));
    }

    @Bean
    public AbstractVoidRequestHandler<MokaItemProcessedRequest> mokaItemEventProcessedRequestHandler(@Qualifier("eventstore") DataSource dataSource
        , AbstractVoidRequestHandler<MokaItemProcessedRequest> handler
        , EventSourceStorage eventSourceStorage
        , LockTrackerStorage lockTrackerStorage) {
        return new MokaEventLock<>(dataSource, lockTrackerStorage, 
                        new MokaCompareEventDate<>(eventSourceStorage,handler));
        }
}
