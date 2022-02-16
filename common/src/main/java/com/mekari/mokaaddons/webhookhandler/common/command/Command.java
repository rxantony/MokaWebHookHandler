package com.mekari.mokaaddons.webhookhandler.common.command;

public interface Command<TCommand> {
    void execute(TCommand cmd)throws CommandException;
}
