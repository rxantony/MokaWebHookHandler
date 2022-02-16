package com.mekari.mokaaddons.webhookhandler.common.consumer;

import com.mekari.mokaaddons.webhookhandler.common.command.Invoker;
import com.rabbitmq.client.Channel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public abstract class AbstractConsumer {

    private @Autowired Invoker invoker;
    private Logger logger;
    private static final String X_REJECTED_INFO = "reason";

    protected AbstractConsumer(){
        init();
    }

    protected AbstractConsumer(Invoker invoker) {
        Assert.notNull(invoker, "invoker must not be null");
        this.invoker = invoker;
        init();
    }

    protected void init(){
        logger = LogManager.getLogger(this.getClass());
    }

    protected Logger getLogger(){
        return logger;
    }

    public void consume(Message message, Channel channel) throws Exception {
        try {
            invoker.invoke(message);
        } catch (Exception ex) {
            // TODO need a thorough research for sending actual error message that cause the
            // message is rejected
            logger.error(ex.getMessage());
            message.getMessageProperties().setAppId("cxdxrx");
            var header = message.getMessageProperties().getHeaders();
            if (header.get(X_REJECTED_INFO) == null)
                header.put(X_REJECTED_INFO, ex.getMessage());
            throw ex;
        }
    }
}
