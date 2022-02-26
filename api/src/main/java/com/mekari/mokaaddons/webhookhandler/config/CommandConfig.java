package com.mekari.mokaaddons.webhookhandler.config;

import javax.sql.DataSource;

import com.mekari.mokaaddons.webhookhandler.common.command.EventCommand;
import com.mekari.mokaaddons.webhookhandler.common.command.moka.MokaEventReceivedCommand;
import com.mekari.mokaaddons.webhookhandler.common.event.moka.MokaJsonEventValidator;
import com.mekari.mokaaddons.webhookhandler.common.storage.DbDeadLetterStorage;
import com.mekari.mokaaddons.webhookhandler.common.storage.DbEventSourceStorage;
import com.mekari.mokaaddons.webhookhandler.common.storage.DeadLetterStorage;
import com.mekari.mokaaddons.webhookhandler.common.storage.EventSourceStorage;
import com.mekari.mokaaddons.webhookhandler.event.MokaItemReceived;
import com.mekari.mokaaddons.webhookhandler.event.MokaTransactionReceived;

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

    @Bean({ "moka.item.added", "moka.item.updated", "moka.item.deleted" })
    public EventCommand<MokaItemReceived> commandItem() {
        return new MokaEventReceivedCommand<MokaItemReceived>(AppConstant.ExchangeName.MOKA_EVENT_RECEIVED_EXCHANGE, MokaItemReceived.class);
    }

    @Bean({ "moka.transaction.added", "moka.transaction.updated", "moka.transaction.deleted" })
    public EventCommand<MokaTransactionReceived> commandTransaction() {
        return new MokaEventReceivedCommand<MokaTransactionReceived>(AppConstant.ExchangeName.MOKA_EVENT_RECEIVED_EXCHANGE, MokaTransactionReceived.class);
    }
}