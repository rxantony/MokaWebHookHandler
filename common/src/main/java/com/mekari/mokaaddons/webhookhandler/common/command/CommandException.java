package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.WebHookHandlingException;

public class CommandException extends WebHookHandlingException {
    public CommandException(String message) {
        super(message);
    }

    public CommandException(Exception inner) {
        super(inner);
    }
}