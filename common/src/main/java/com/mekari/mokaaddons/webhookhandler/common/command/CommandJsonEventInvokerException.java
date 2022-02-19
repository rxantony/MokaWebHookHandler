package com.mekari.mokaaddons.webhookhandler.common.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.mekari.mokaaddons.webhookhandler.common.WebHookHandlingException;
import com.mekari.mokaaddons.webhookhandler.common.event.Event;

public class CommandJsonEventInvokerException extends WebHookHandlingException {
    public final String event;
    public final JsonNode eventNode;
    public final Event eventObj;

    public CommandJsonEventInvokerException(String event, JsonNode eventNode, Event eventObj, Exception cause) {
        super(cause);
        this.event = event;
        this.eventNode = eventNode;
        this.eventObj = eventObj;
    }
}