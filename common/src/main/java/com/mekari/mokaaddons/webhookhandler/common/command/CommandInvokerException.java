package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.WebHookHandlingException;

public class CommandInvokerException extends WebHookHandlingException {
    public final String param;

    public CommandInvokerException(String param, Throwable cause) {
        super(cause);
        this.param = param;
    }
}