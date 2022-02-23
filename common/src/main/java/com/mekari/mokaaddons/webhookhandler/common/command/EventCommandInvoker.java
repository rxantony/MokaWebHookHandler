package com.mekari.mokaaddons.webhookhandler.common.command;

public interface EventCommandInvoker {
    void invoke(String message) throws EventCommandInvokerException;
}
