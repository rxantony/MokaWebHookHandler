package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public final class SpringbootCommandManager implements CommandManager {
    private @Autowired ApplicationContext appContext;

    /**
     * do not use this default constructor, using another parameterized constructors
     * for manual instantiation instead.
     * this constuctor is neccessary by springboot to instantiate this class.
     */
    public SpringbootCommandManager() {
    }

    public SpringbootCommandManager(ApplicationContext appContext) {
        Assert.notNull(appContext, "appContext");
        this.appContext = appContext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TEvent extends Event> AbstractCommand<TEvent> createCommand(String eventName) throws Exception {
        return (AbstractCommand<TEvent>) appContext.getBean(eventName, AbstractCommand.class);
    }
}
