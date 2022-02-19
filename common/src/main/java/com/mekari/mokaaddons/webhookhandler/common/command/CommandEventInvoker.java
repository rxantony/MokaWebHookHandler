package com.mekari.mokaaddons.webhookhandler.common.command;

public interface CommandEventInvoker {
    void invoke(String message) throws CommandEventInvokerException;
}
