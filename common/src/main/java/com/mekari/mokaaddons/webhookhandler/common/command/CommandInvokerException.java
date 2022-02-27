package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.WebHookHandlingException;

public class CommandInvokerException extends WebHookHandlingException {
    public final String command;

    public CommandInvokerException(String command, Throwable cause) {
        super(cause);
        this.command = command;
    }
}