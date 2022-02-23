package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public final class SpringbootEventCommandManager implements EventCommandManager {
    private @Autowired ApplicationContext appContext;

    /**
     * do not use this default constructor, using another parameterized constructors
     * for manual instantiation instead.
     * this constuctor is neccessary by springboot to instantiate this class.
     */
    public SpringbootEventCommandManager() {
    }

    public SpringbootEventCommandManager(ApplicationContext appContext) {
        Assert.notNull(appContext, "appContext");
        this.appContext = appContext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TEvent extends Event> EventCommand<TEvent> createCommand(String eventName)
            throws ClassNotFoundException {
        return (EventCommand<TEvent>) appContext.getBean(eventName, EventCommand.class);
    }
}
