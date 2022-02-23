package com.mekari.mokaaddons.webhookhandler.common.consumer;

import com.mekari.mokaaddons.webhookhandler.common.command.EventCommandInvoker;
import com.rabbitmq.client.Channel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public abstract class AbstractConsumer {
    
    private @Autowired EventCommandInvoker invoker;
    private Logger logger;

    /**
     * do not use this default constructor, using another parameterized constructors
     * for manual instantiation instead.
     * this constuctor is neccessary by springboot to instantiate this class.
     */
    protected AbstractConsumer() {
        init();
    }

    protected AbstractConsumer(EventCommandInvoker invoker) {
        Assert.notNull(invoker, "invoker must not be null");
        this.invoker = invoker;
        init();
    }

    protected void init() {
        logger = LogManager.getLogger(this.getClass());
    }

    protected final Logger getLogger() {
        return logger;
    }

    public void consume(Message message, Channel channel) throws Exception{
        var msg = new String(message.getBody());
        invoker.invoke(msg);
    }
}
