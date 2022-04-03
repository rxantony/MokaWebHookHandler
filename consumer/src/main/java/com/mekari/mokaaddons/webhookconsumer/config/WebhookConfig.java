package com.mekari.mokaaddons.webhookconsumer.config;

import javax.sql.DataSource;

import com.mekari.mokaaddons.common.webhook.Command;
import com.mekari.mokaaddons.common.webhook.DeadLetterStorage;
import com.mekari.mokaaddons.common.webhook.EventSourceStorage;
import com.mekari.mokaaddons.common.webhook.LockTrackerStorage;
import com.mekari.mokaaddons.common.webhook.moka.MokaCompareEventDateCommand;
import com.mekari.mokaaddons.common.webhook.moka.MokaEventLockCommand;
import com.mekari.mokaaddons.common.webhook.storage.DbDeadLetterStorage;
import com.mekari.mokaaddons.common.webhook.storage.DbEventSourceStorage;
import com.mekari.mokaaddons.common.webhook.storage.DbLockTrackerStorage;
import com.mekari.mokaaddons.webhookconsumer.webhook.mokaitemprocessed.MokaItemReceivedEvent;
import com.mekari.mokaaddons.webhookconsumer.webhook.mokaitemreceived.MokaItemProcessedEvent;

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
    
    @Bean({ "moka.item.added", "moka.item.updated", "moka.item.deleted" })
    public Command<MokaItemReceivedEvent> commandMokaItemEventReceived(@Qualifier("eventstore") DataSource dataSource
        , Command<MokaItemReceivedEvent> command
        , EventSourceStorage eventSourceStorage
        , LockTrackerStorage lockTrackerStorage) {
        //return command;
        // chain of responsibilty here
        //return new CommandEventUpdateAtValidation<>(eventSourceStorage, command);
        //return new DBCommandEventLock<>(dataSource, lockTrackerStorage, command);
        return new MokaCompareEventDateCommand<>(eventSourceStorage,
                new MokaEventLockCommand<>(dataSource, lockTrackerStorage,
                    new MokaCompareEventDateCommand<>(eventSourceStorage, command)));

        //return new MokaEventLockCommand<>(dataSource, lockTrackerStorage, command);
    }

    @Bean({ "moka.item.processed" })
    public Command<MokaItemProcessedEvent> commandMokaItemEventProcessed(@Qualifier("eventstore") DataSource dataSource
        , Command<MokaItemProcessedEvent> command
        , EventSourceStorage eventSourceStorage
        , LockTrackerStorage lockTrackerStorage) {
        // chain of responsibilty here
        //return new CommandEventUpdateAtValidation<>(eventSourceStorage, command);
        //return new DBCommandEventLock<>(dataSource, lockTrackerStorage, command);
        return new MokaCompareEventDateCommand<>(eventSourceStorage,
                new MokaEventLockCommand<>(dataSource, lockTrackerStorage,
                        new MokaCompareEventDateCommand<>(eventSourceStorage, command)));
    }
}
