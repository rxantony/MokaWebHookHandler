package com.mekari.mokaaddons.common.command;

public interface Command<T> {
    void execute(T command) throws Exception;
}
