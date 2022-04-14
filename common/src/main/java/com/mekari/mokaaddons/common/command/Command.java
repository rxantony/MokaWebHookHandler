package com.mekari.mokaaddons.common.command;

public interface Command<TParam> {
    Class<TParam>  paramClass();
    void execute(TParam command) throws Exception;
}
