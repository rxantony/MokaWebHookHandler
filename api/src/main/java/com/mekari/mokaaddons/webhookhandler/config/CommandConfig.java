package com.mekari.mokaaddons.webhookhandler.config;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.webhookhandler.common.command.EventCommand;
import com.mekari.mokaaddons.webhookhandler.common.command.moka.MokaEventReceivedCommand;
import com.mekari.mokaaddons.webhookhandler.common.command.moka.MokaEventReceivedCommand.Config;
import com.mekari.mokaaddons.webhookhandler.common.event.moka.MokaJsonEventValidator;
import com.mekari.mokaaddons.webhookhandler.common.storage.DbDeadLetterStorage;
import com.mekari.mokaaddons.webhookhandler.common.storage.DbEventSourceStorage;
import com.mekari.mokaaddons.webhookhandler.common.storage.DeadLetterStorage;
import com.mekari.mokaaddons.webhookhandler.common.storage.EventSourceStorage;
import com.mekari.mokaaddons.webhookhandler.event.MokaItemReceived;
import com.mekari.mokaaddons.webhookhandler.event.MokaTransactionReceived;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class CommandConfig {

    @Bean
    @Primary
    public MokaJsonEventValidator mokaEventValidator(){
        return new MokaJsonEventValidator();
    }

    @Bean
    public EventSourceStorage eventStoreStorage(DataSource dataSource){
        return new DbEventSourceStorage(dataSource);
    }

    @Bean
    public DeadLetterStorage deadLetterConsumerConfig(DataSource dataSource){
        return new DbDeadLetterStorage(dataSource);
    }

    @Bean
    public Config eventConfig(EventSourceStorage eventSourceStorage
        , AmqpTemplate  amqpTemplate
        , ObjectMapper mapper){
        return new Config(AppConstant.ExchangeName.MOKA_EVENT_RECEIVED_EXCHANGE, eventSourceStorage, amqpTemplate, mapper);
    }

    @Bean({ "moka.item.added", "moka.item.updated", "moka.item.deleted" })
    public EventCommand<MokaItemReceived> commandItem(Config config) {
        return new MokaEventReceivedCommand<MokaItemReceived>(config, MokaItemReceived.class);
    }

    @Bean({ "moka.transaction.added", "moka.transaction.updated", "moka.transaction.deleted" })
    public EventCommand<MokaTransactionReceived> commandTransaction(Config config) {
        return new MokaEventReceivedCommand<MokaTransactionReceived>(config, MokaTransactionReceived.class);
    }
}