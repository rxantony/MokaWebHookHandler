package com.mekari.mokaaddons.webhookhandler.config;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.webhookhandler.common.consumer.AbstractDeadLetterConsumer.Config;
import com.mekari.mokaaddons.webhookhandler.common.storage.DbDeadLetterStorage;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsumerConfig {

    @Bean
    public Config deadLetterConsumerConfig(@Qualifier("eventstore") DataSource dataSource
        , AmqpTemplate  amqpTemplate
        , ObjectMapper mapper){
        return new Config(new DbDeadLetterStorage(dataSource), amqpTemplate, mapper, 4);
    }
}
