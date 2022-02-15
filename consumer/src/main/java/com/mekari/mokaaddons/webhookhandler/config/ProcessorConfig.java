package com.mekari.mokaaddons.webhookhandler.config;

import javax.sql.DataSource;

import com.mekari.mokaaddons.webhookhandler.common.processor.DbEventLockProcessor;
import com.mekari.mokaaddons.webhookhandler.common.processor.DbEventUpdateAtValidationProcessor;
import com.mekari.mokaaddons.webhookhandler.common.processor.EventProcessor;
import com.mekari.mokaaddons.webhookhandler.common.storage.DbLockTrackerStorage;
import com.mekari.mokaaddons.webhookhandler.common.storage.LockTrackerStorage;
import com.mekari.mokaaddons.webhookhandler.event.MokaItemProcessed;
import com.mekari.mokaaddons.webhookhandler.event.MokaItemReceived;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessorConfig {

    @Bean
    public LockTrackerStorage dbLockTracker(@Qualifier("eventstore") DataSource dataSource) {
        return new DbLockTrackerStorage(dataSource);
    }

    @Bean({ "moka.item.added", "moka.item.updated", "moka.item.deleted" })
    public EventProcessor<MokaItemReceived> mokaItemEventReceivedProcessor(@Qualifier("eventstore") DataSource dataSource
        , EventProcessor<MokaItemReceived> processor
        , LockTrackerStorage lockTracker) {
        // chain of responsibilty here
        //return  new DbEventLockProcessor<>(dataSource, lockTracker,processor);
        
        return new DbEventUpdateAtValidationProcessor<>(dataSource,
                new DbEventLockProcessor<>(dataSource, lockTracker,
                        new DbEventUpdateAtValidationProcessor<>(dataSource, processor)));
    }

    @Bean({ "moka.item.processed" })
    public EventProcessor<MokaItemProcessed> mokaItemEventProcessedProcessor(@Qualifier("eventstore") DataSource dataSource
        , EventProcessor<MokaItemProcessed> processor
        , LockTrackerStorage lockTracker) {
        // chain of responsibilty here
        //return  new DbEventLockProcessor<>(dataSource, lockTracker,processor);
        return new DbEventUpdateAtValidationProcessor<>(dataSource,
                new DbEventLockProcessor<>(dataSource, lockTracker,
                        new DbEventUpdateAtValidationProcessor<>(dataSource, processor)));
    }
}
