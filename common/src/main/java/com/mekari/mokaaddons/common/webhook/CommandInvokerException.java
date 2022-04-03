package com.mekari.mokaaddons.common.webhook;

import com.fasterxml.jackson.databind.JsonNode;

public class CommandInvokerException extends WebhookHandlingException {
    public final JsonNode eventNode;

    public CommandInvokerException(String event, JsonNode eventNode, Throwable cause) {
        super(event, cause);
        this.eventNode = eventNode;
    }
}