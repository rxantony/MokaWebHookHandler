package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringbootCommandManager implements CommandManager {
    @Autowired
    private ApplicationContext appContext;

    @Override
    @SuppressWarnings("unchecked")
    public <TEvent extends Event> CommandEvent<TEvent> createCommand(String eventName)
            throws ClassNotFoundException {
        return (CommandEvent<TEvent>) appContext.getBean(eventName, CommandEvent.class);
    }
}
