package com.mekari.mokaaddons.webhookhandler.common.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.mekari.mokaaddons.webhookhandler.common.event.Event;

public class JsonEventCommandInvokerException extends CommandInvokerException {
    public final JsonNode eventNode;
    public final Event eventObj;

    public JsonEventCommandInvokerException(String command, JsonNode eventNode, Event eventObj, Throwable cause) {
        super(command, cause);
        this.eventNode = eventNode;
        this.eventObj = eventObj;
    }
}