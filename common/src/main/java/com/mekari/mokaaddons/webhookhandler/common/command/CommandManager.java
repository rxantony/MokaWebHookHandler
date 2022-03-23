package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;

public interface CommandManager {
     <TEvent extends Event> Command<TEvent> createCommand(String eventName) throws Exception;
}
