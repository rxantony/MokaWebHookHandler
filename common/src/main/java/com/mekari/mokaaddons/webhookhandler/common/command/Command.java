package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;

public interface Command<TEvent extends Event> {
    Class<TEvent> eventClass();
    void execute(TEvent param)throws Exception;
}
