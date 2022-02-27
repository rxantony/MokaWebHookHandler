package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;

public interface EventCommandManager {
     <TEvent extends Event> EventCommand<TEvent> createCommand(String eventName) throws Exception;
}
