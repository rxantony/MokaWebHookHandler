package com.mekari.mokaaddons.webhookhandler.config;

import javax.sql.DataSource;

import com.mekari.mokaaddons.webhookhandler.common.command.DBCommandEventLock;
import com.mekari.mokaaddons.webhookhandler.common.command.CommandEvent;
import com.mekari.mokaaddons.webhookhandler.common.command.CommandEventUpdateAtValidation;
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
    public CommandEvent<MokaItemReceived> commandMokaItemEventReceived(@Qualifier("eventstore") DataSource dataSource
        , CommandEvent<MokaItemReceived> command
        , DbEventSourceStorage eventSourceStorage
        , LockTrackerStorage lockTrackerStorage) {
        // chain of responsibilty here
        return new CommandEventUpdateAtValidation<>(eventSourceStorage,
                new DBCommandEventLock<>(dataSource, lockTrackerStorage,
                        new CommandEventUpdateAtValidation<>(eventSourceStorage, command)));
    }

    @Bean({ "moka.item.processed" })
    public CommandEvent<MokaItemProcessed> commandMokaItemEventProcessed(@Qualifier("eventstore") DataSource dataSource
        , CommandEvent<MokaItemProcessed> command
        , DbEventSourceStorage eventSourceStorage
        , LockTrackerStorage lockTracker) {
        // chain of responsibilty here
        return new CommandEventUpdateAtValidation<>(eventSourceStorage,
                new DBCommandEventLock<>(dataSource, lockTracker,
                        new CommandEventUpdateAtValidation<>(eventSourceStorage, command)));
    }
}
