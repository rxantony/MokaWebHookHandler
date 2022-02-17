package com.mekari.mokaaddons.webhookhandler.common.event;

import com.mekari.mokaaddons.webhookhandler.common.command.CommandException;

public class UnknownEventFormatException extends CommandException {

    private final String event;

    public UnknownEventFormatException(String message, String event) {
        super(message);
        this.event = event;
    }

    public String Event() {
        return event;
    }
}
