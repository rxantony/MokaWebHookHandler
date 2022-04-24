package com.mekari.mokaaddons.common.command;

public interface Command<TParam> {
    void execute(TParam command) throws Exception;
}
