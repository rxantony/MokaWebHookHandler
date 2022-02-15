package com.mekari.mokaaddons.webhookhandler.config;

import javax.sql.DataSource;

import com.mekari.mokaaddons.webhookhandler.common.processor.DbEventLockProcessor;
import com.mekari.mokaaddons.webhookhandler.common.processor.EventUpdateAtValidationProcessor;
import com.mekari.mokaaddons.webhookhandler.common.processor.EventProcessor;
import com.mekari.mokaaddons.webhookhandler.common.storage.DbEventSourceStorage;
import com.mekari.mokaaddons.webhookhandler.common.storage.DbLockTrackerStorage;
import com.mekari.mokaaddons.webhookhandler.common.storage.EventSourceStorage;
import com.mekari.mokaaddons.webhookhandler.common.storage.LockTrackerStorage;
import com.mekari.mokaaddons.webhookhandler.event.MokaItemProcessed;
import com.mekari.mokaaddons.webhookhandler.event.MokaItemReceived;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessorConfig {

    @Bean
    public EventSourceStorage eventSourceStorage(@Qualifier("eventstore") DataSource dataSource) {
        return new DbEventSourceStorage(dataSource);
    }

    @Bean
    public LockTrackerStorage lockTrackerStorage(@Qualifier("eventstore") DataSource dataSource) {
        return new DbLockTrackerStorage(dataSource);
    }

    @Bean({ "moka.item.added", "moka.item.updated", "moka.item.deleted" })
    public EventProcessor<MokaItemReceived> mokaItemEventReceivedProcessor(@Qualifier("eventstore") DataSource dataSource
        , EventProcessor<MokaItemReceived> processor
        , DbEventSourceStorage eventSourceStorage
        , LockTrackerStorage lockTrackerStorage) {
        // chain of responsibilty here
        //return  new DbEventLockProcessor<>(dataSource, lockTracker,processor);
        
        return new EventUpdateAtValidationProcessor<>(eventSourceStorage,
                new DbEventLockProcessor<>(dataSource, lockTrackerStorage,
                        new EventUpdateAtValidationProcessor<>(eventSourceStorage, processor)));
    }

    @Bean({ "moka.item.processed" })
    public EventProcessor<MokaItemProcessed> mokaItemEventProcessedProcessor(@Qualifier("eventstore") DataSource dataSource
        , EventProcessor<MokaItemProcessed> processor
        , DbEventSourceStorage eventSourceStorage
        , LockTrackerStorage lockTracker) {
        // chain of responsibilty here
        //return  new DbEventLockProcessor<>(dataSource, lockTracker,processor);
        return new EventUpdateAtValidationProcessor<>(eventSourceStorage,
                new DbEventLockProcessor<>(dataSource, lockTracker,
                        new EventUpdateAtValidationProcessor<>(eventSourceStorage, processor)));
    }
}
