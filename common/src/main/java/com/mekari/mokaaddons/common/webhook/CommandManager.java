package com.mekari.mokaaddons.common.webhook;

public interface CommandManager {
     <TEvent extends Event> Command<TEvent> createCommand(String eventName) throws Exception;
}
