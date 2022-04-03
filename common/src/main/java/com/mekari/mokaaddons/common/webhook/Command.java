package com.mekari.mokaaddons.common.webhook;

public interface Command<TEvent extends Event> {
    Class<TEvent> eventClass();
    void execute(TEvent param)throws Exception;
}
