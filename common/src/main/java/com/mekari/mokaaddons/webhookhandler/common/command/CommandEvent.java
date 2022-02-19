package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;

public interface CommandEvent<TEvent extends Event> extends Command<TEvent> {
    Class<TEvent> eventClass();

    void execute(TEvent event) throws CommandEventException;
}
