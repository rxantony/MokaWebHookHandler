package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

public abstract class AbstractCommandEvent<TEvent extends Event> implements CommandEvent<TEvent> {

    private final Class<TEvent> eventCls;
    protected final Logger logger;

    protected AbstractCommandEvent(Class<TEvent> eventCls) {
        Assert.notNull(eventCls, "eventCls must not be null");
        this.eventCls = eventCls;
        logger = LogManager.getFormatterLogger(this.getClass());
    }

    @Override
    public Class<TEvent> eventClass() {
        return eventCls;
    }

    @Override
    abstract public void execute(TEvent event) throws CommandException;
}
