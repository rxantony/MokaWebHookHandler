package com.mekari.mokaaddons.webhookhandler.config;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.webhookhandler.common.command.CommandEvent;
import com.mekari.mokaaddons.webhookhandler.common.command.CommandMokaEventReceived;
import com.mekari.mokaaddons.webhookhandler.common.command.CommandMokaEventReceived.Config;
import com.mekari.mokaaddons.webhookhandler.common.storage.DbDeadLetterStorage;
import com.mekari.mokaaddons.webhookhandler.common.storage.DeadLetterStorage;
import com.mekari.mokaaddons.webhookhandler.event.MokaItemReceived;
import com.mekari.mokaaddons.webhookhandler.event.MokaTransactionReceived;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandConfig {

    @Bean
    public DeadLetterStorage deadLetterConsumerConfig(DataSource dataSource){
        return new DbDeadLetterStorage(dataSource);
    }

    @Bean
    public Config eventConfig(DataSource dataSource
        , AmqpTemplate  amqpTemplate
        , ObjectMapper mapper){
        return new Config(AppConstant.ExchangeName.MOKA_EVENT_RECEIVED_EXCHANGE, dataSource, amqpTemplate, mapper);
    }

    @Bean({ "moka.item.added", "moka.item.updated", "moka.item.deleted" })
    public CommandEvent<MokaItemReceived> commandItem(Config config) {
        return new CommandMokaEventReceived<MokaItemReceived>(config, MokaItemReceived.class);
    }

    @Bean({ "moka.transaction.added", "moka.transaction.updated", "moka.transaction.deleted" })
    public CommandEvent<MokaTransactionReceived> commandTransaction(Config config) {
        return new CommandMokaEventReceived<MokaTransactionReceived>(config, MokaTransactionReceived.class);
    }
}