package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;

public interface CommandEventManager {
     <TEvent extends Event> CommandEvent<TEvent> createCommand(String eventName) throws ClassNotFoundException;
}
