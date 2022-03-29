package com.mekari.mokaaddons.webhookhandler.config;

import javax.sql.DataSource;

import com.mekari.mokaaddons.webhookhandler.common.storage.DbDeadLetterStorage;
import com.mekari.mokaaddons.webhookhandler.common.storage.DbEventSourceStorage;
import com.mekari.mokaaddons.webhookhandler.common.storage.DeadLetterStorage;
import com.mekari.mokaaddons.webhookhandler.common.storage.EventSourceStorage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageAndValidatorConfig {

    @Bean
    public EventSourceStorage eventStoreStorage(DataSource dataSource){
        return new DbEventSourceStorage(dataSource);
    }

    @Bean
    public DeadLetterStorage deadLetterConsumerConfig(DataSource dataSource){
        return new DbDeadLetterStorage(dataSource);
    }
}