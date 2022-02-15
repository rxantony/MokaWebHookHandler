package com.mekari.mokaaddons.webhookhandler.config;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.webhookhandler.common.processor.EventProcessor;
import com.mekari.mokaaddons.webhookhandler.common.processor.MokaEventReceivedProcessor;
import com.mekari.mokaaddons.webhookhandler.common.processor.MokaEventReceivedProcessor.Config;
import com.mekari.mokaaddons.webhookhandler.event.MokaItemReceived;
import com.mekari.mokaaddons.webhookhandler.event.MokaTransactionReceived;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessorConfig {

    @Bean
    public Config eventProcessorConfig(DataSource dataSource
        , AmqpTemplate  amqpTemplate
        , ObjectMapper mapper){
        return new Config(AppConstant.EventName.MOKA_EVENT_RECEIVED, dataSource, amqpTemplate, mapper);
    }

    @Bean({ "moka.item.added", "moka.item.updated", "moka.item.deleted" })
    public EventProcessor<MokaItemReceived> itemProcessor(Config config) {
        return new MokaEventReceivedProcessor<MokaItemReceived>(config, MokaItemReceived.class);
    }

    @Bean({ "moka.transaction.added", "moka.transaction.updated", "moka.transaction.deleted" })
    public EventProcessor<MokaTransactionReceived> transactionProcessor(Config config) {
        return new MokaEventReceivedProcessor<MokaTransactionReceived>(config, MokaTransactionReceived.class);
    }
}