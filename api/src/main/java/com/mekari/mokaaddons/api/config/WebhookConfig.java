package com.mekari.mokaaddons.api.config;

import javax.sql.DataSource;

import com.mekari.mokaaddons.api.service.outlet.checkoutlet.v2.CheckOutletRequestHandler;
import com.mekari.mokaaddons.api.webhook.event.MokaItemReceivedEvent;
import com.mekari.mokaaddons.api.webhook.event.MokaTransactionReceivedEvent;
import com.mekari.mokaaddons.common.webhook.DeadLetterStorage;
import com.mekari.mokaaddons.common.webhook.EventSourceStorage;
import com.mekari.mokaaddons.common.webhook.moka.EventNameClassMap;
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


    @Bean
    public EventNameClassMap eventClassMap() {
        return new EventNameClassMap()
                .add("moka.item.added", MokaItemReceivedEvent.class, null)
                .add("moka.item.updated", MokaItemReceivedEvent.class, null)
                .add("moka.item.deleted", MokaItemReceivedEvent.class, null)
                .add("moka.transaction.created", MokaTransactionReceivedEvent.class, null)
                .add("moka.transaction.updated", MokaTransactionReceivedEvent.class, null)
                .add("moka.transaction.refunded", MokaTransactionReceivedEvent.class, null);
    }

    @Bean
    public CheckOutletRequestHandler chechOutlet(){
        return new CheckOutletRequestHandler();
    }
}