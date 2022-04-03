package com.mekari.mokaaddons.api.config;

import javax.sql.DataSource;

import com.mekari.mokaaddons.api.webhook.event.MokaItemReceived;
import com.mekari.mokaaddons.api.webhook.event.MokaTransactionReceived;
import com.mekari.mokaaddons.common.webhook.Command;
import com.mekari.mokaaddons.common.webhook.DeadLetterStorage;
import com.mekari.mokaaddons.common.webhook.EventSourceStorage;
import com.mekari.mokaaddons.common.webhook.moka.MokaSaveAndPublishEventCommand;
import com.mekari.mokaaddons.common.webhook.storage.DbDeadLetterStorage;
import com.mekari.mokaaddons.common.webhook.storage.DbEventSourceStorage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebhookConfig {
    @Bean
    public EventSourceStorage eventStoreStorage(DataSource dataSource) {
        return new DbEventSourceStorage(dataSource);
    }

    @Bean
    public DeadLetterStorage deadLetterConsumerConfig(DataSource dataSource) {
        return new DbDeadLetterStorage(dataSource);
    }

    @Bean({ "moka.item.added", "moka.item.updated", "moka.item.deleted" })
    public Command<MokaItemReceived> commandItem() {
        return new MokaSaveAndPublishEventCommand<>(AppConstant.ExchangeName.MOKA_EVENT_RECEIVED_EXCHANGE,
                MokaItemReceived.class);
    }

    @Bean({ "moka.transaction.added", "moka.transaction.updated", "moka.transaction.deleted" })
    public Command<MokaTransactionReceived> commandTransaction() {
        return new MokaSaveAndPublishEventCommand<>(AppConstant.ExchangeName.MOKA_EVENT_RECEIVED_EXCHANGE,
                MokaTransactionReceived.class);
    }
}