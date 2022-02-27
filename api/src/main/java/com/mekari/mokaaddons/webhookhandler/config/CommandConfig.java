package com.mekari.mokaaddons.webhookhandler.config;

import com.mekari.mokaaddons.webhookhandler.common.command.EventCommand;
import com.mekari.mokaaddons.webhookhandler.common.command.moka.SaveToDbAndPublishCommand;
import com.mekari.mokaaddons.webhookhandler.event.MokaItemReceived;
import com.mekari.mokaaddons.webhookhandler.event.MokaTransactionReceived;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandConfig {

    @Bean({ "moka.item.added", "moka.item.updated", "moka.item.deleted" })
    public EventCommand<MokaItemReceived> commandItem() {
        return new SaveToDbAndPublishCommand<MokaItemReceived>(AppConstant.ExchangeName.MOKA_EVENT_RECEIVED_EXCHANGE, MokaItemReceived.class);
    }

    @Bean({ "moka.transaction.added", "moka.transaction.updated", "moka.transaction.deleted" })
    public EventCommand<MokaTransactionReceived> commandTransaction() {
        return new SaveToDbAndPublishCommand<MokaTransactionReceived>(AppConstant.ExchangeName.MOKA_EVENT_RECEIVED_EXCHANGE, MokaTransactionReceived.class);
    }
}