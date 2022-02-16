package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;

public interface CommandEvent<TEvent extends Event> extends Command<TEvent>{
    void execute(TEvent event) throws CommandException;

    Class<TEvent> eventClass();
}
