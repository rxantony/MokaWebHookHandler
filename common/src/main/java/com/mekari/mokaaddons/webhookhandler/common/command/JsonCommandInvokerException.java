package com.mekari.mokaaddons.webhookhandler.common.command;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonCommandInvokerException extends CommandInvokerException {
    public final JsonNode eventNode;

    public JsonCommandInvokerException(String event, JsonNode eventNode, Throwable cause) {
        super(event, cause);
        this.eventNode = eventNode;
    }
}