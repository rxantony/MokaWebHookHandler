package com.mekari.mokaaddons.webhookhandler.config;

import javax.sql.DataSource;

import com.mekari.mokaaddons.webhookhandler.common.command.EventCommand;
import com.mekari.mokaaddons.webhookhandler.common.command.EventDateCompareCommand;
import com.mekari.mokaaddons.webhookhandler.common.command.DBEventLockCommand;
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
public class CommandConfig {

    @Bean
    public EventSourceStorage eventSourceStorage(@Qualifier("eventstore") DataSource dataSource) {
        return new DbEventSourceStorage(dataSource);
    }

    @Bean
    public LockTrackerStorage lockTrackerStorage(@Qualifier("eventstore") DataSource dataSource) {
        return new DbLockTrackerStorage(dataSource);
    }

    @Bean({ "moka.item.added", "moka.item.updated", "moka.item.deleted" })
    public EventCommand<MokaItemReceived> commandMokaItemEventReceived(@Qualifier("eventstore") DataSource dataSource
        , EventCommand<MokaItemReceived> command
        , DbEventSourceStorage eventSourceStorage
        , LockTrackerStorage lockTrackerStorage) {
        // chain of responsibilty here
        //return new CommandEventUpdateAtValidation<>(eventSourceStorage, command);
        //return new DBCommandEventLock<>(dataSource, lockTrackerStorage, command);
        return new EventDateCompareCommand<>(eventSourceStorage,
                new DBEventLockCommand<>(dataSource, lockTrackerStorage,
                    new EventDateCompareCommand<>(eventSourceStorage, command)));
    }

    @Bean({ "moka.item.processed" })
    public EventCommand<MokaItemProcessed> commandMokaItemEventProcessed(@Qualifier("eventstore") DataSource dataSource
        , EventCommand<MokaItemProcessed> command
        , DbEventSourceStorage eventSourceStorage
        , LockTrackerStorage lockTrackerStorage) {
        // chain of responsibilty here
        //return new CommandEventUpdateAtValidation<>(eventSourceStorage, command);
        //return new DBCommandEventLock<>(dataSource, lockTrackerStorage, command);
        return new EventDateCompareCommand<>(eventSourceStorage,
                new DBEventLockCommand<>(dataSource, lockTrackerStorage,
                        new EventDateCompareCommand<>(eventSourceStorage, command)));
    }
}
