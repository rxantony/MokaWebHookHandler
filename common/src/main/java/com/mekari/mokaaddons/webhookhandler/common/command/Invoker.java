package com.mekari.mokaaddons.webhookhandler.common.command;

import org.springframework.amqp.core.Message;

public interface Invoker {
    void invoke(Message message) throws Exception;
}
