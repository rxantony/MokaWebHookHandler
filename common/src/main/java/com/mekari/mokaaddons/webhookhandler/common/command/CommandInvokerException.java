package com.mekari.mokaaddons.webhookhandler.common.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.mekari.mokaaddons.webhookhandler.common.WebHookHandlingException;

public class CommandInvokerException extends WebHookHandlingException {
    public final JsonNode eventNode;

    public CommandInvokerException(String event, JsonNode eventNode, Throwable cause) {
        super(event, cause);
        this.eventNode = eventNode;
    }
}