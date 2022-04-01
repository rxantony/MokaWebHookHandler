package com.mekari.mokaaddons.webhookhandler.config;

import javax.sql.DataSource;

import com.mekari.mokaaddons.webhookhandler.common.command.Command;
import com.mekari.mokaaddons.webhookhandler.common.command.MokaCompareEventDateCommand;
import com.mekari.mokaaddons.webhookhandler.common.command.MokaEventLockCommand;
import com.mekari.mokaaddons.webhookhandler.common.storage.EventSourceStorage;
import com.mekari.mokaaddons.webhookhandler.common.storage.LockTrackerStorage;
import com.mekari.mokaaddons.webhookhandler.event.MokaItemProcessed;
import com.mekari.mokaaddons.webhookhandler.event.MokaItemReceived;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandConfig {

    @Bean({ "moka.item.added", "moka.item.updated", "moka.item.deleted" })
    public Command<MokaItemReceived> commandMokaItemEventReceived(@Qualifier("eventstore") DataSource dataSource
        , Command<MokaItemReceived> command
        , EventSourceStorage eventSourceStorage
        , LockTrackerStorage lockTrackerStorage) {
        return command;
        // chain of responsibilty here
        //return new CommandEventUpdateAtValidation<>(eventSourceStorage, command);
        //return new DBCommandEventLock<>(dataSource, lockTrackerStorage, command);
        /*return new MokaCompareEventDateCommand<>(eventSourceStorage,
                new MokaEventLockCommand<>(dataSource, lockTrackerStorage,
                    new MokaCompareEventDateCommand<>(eventSourceStorage, command)));*/
    }

    @Bean({ "moka.item.processed" })
    public Command<MokaItemProcessed> commandMokaItemEventProcessed(@Qualifier("eventstore") DataSource dataSource
        , Command<MokaItemProcessed> command
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
