package com.mekari.mokaaddons.webhookhandler.common.command;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.stereotype.Component;

@Component
public class DefaultJsonEventValidator implements JsonEventValidator {

    @Override
    public void validate(JsonNode eventNode) throws UnknownEventFormatException {
        var headerNode = eventNode.get("header");
        if (headerNode == null)
            throw new UnknownEventFormatException("header is required", eventNode.toString());

        var eventIdNode = headerNode.get("event_id");
        if (eventIdNode == null)
            throw new UnknownEventFormatException("eventId is required", eventNode.toString());

        if (headerNode.get("event_name") == null)
            throw new UnknownEventFormatException(
                String.format("event_name is required for eventId:%s", eventIdNode.asText()), eventNode.toString());

        if(headerNode.get("timestamp") == null)
            throw new UnknownEventFormatException(
                String.format("header.timestamp is required for eventId:%s", eventIdNode.asText()), eventNode.toString());

        var bodyNode = eventNode.get("body");
        if (bodyNode == null)
            throw new UnknownEventFormatException(
                    String.format("body is required for eventId:%s", eventIdNode.asText()), eventNode.toString());

        JsonNode dataNode = null;
        var elems = bodyNode.elements();
        if(elems.hasNext()) {
            dataNode = elems.next();
        }
        
        if (dataNode == null)
            throw new UnknownEventFormatException(
                    String.format("body data is required for eventId:%s", eventIdNode.asText()), eventNode.toString());

        if (dataNode.get("id") == null)
            throw new UnknownEventFormatException(
                    String.format("body data id is required for eventId:%s", eventIdNode.asText()),
                    eventNode.toString());
    }
    
}
