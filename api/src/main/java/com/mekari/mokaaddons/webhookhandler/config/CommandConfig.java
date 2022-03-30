package com.mekari.mokaaddons.webhookhandler.config;

import com.mekari.mokaaddons.webhookhandler.common.command.Command;
import com.mekari.mokaaddons.webhookhandler.common.command.MokaSaveAndPublishEventCommand;
import com.mekari.mokaaddons.webhookhandler.event.MokaItemReceived;
import com.mekari.mokaaddons.webhookhandler.event.MokaTransactionReceived;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandConfig {

    @Bean({ "moka.item.added", "moka.item.updated", "moka.item.deleted" })
    public Command<MokaItemReceived> commandItem() {
        return new MokaSaveAndPublishEventCommand<MokaItemReceived>(AppConstant.ExchangeName.MOKA_EVENT_RECEIVED_EXCHANGE, MokaItemReceived.class);
    }

    @Bean({ "moka.transaction.added", "moka.transaction.updated", "moka.transaction.deleted" })
    public Command<MokaTransactionReceived> commandTransaction() {
        return new MokaSaveAndPublishEventCommand<MokaTransactionReceived>(AppConstant.ExchangeName.MOKA_EVENT_RECEIVED_EXCHANGE, MokaTransactionReceived.class);
    }
}