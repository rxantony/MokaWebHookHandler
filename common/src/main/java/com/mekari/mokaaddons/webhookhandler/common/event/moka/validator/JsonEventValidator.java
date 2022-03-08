package com.mekari.mokaaddons.webhookhandler.common.event.moka.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.mekari.mokaaddons.webhookhandler.common.command.UnknownEventFormatException;
import com.mekari.mokaaddons.webhookhandler.common.event.validator.JsonEventValidatorDefault;

public class JsonEventValidator extends JsonEventValidatorDefault{

    public void validate(JsonNode eventNode) throws UnknownEventFormatException {
        super.validate(eventNode);

        var headerNode = eventNode.get("header");
        var eventIdNode = headerNode.get("event_id");
        
        if(headerNode.get("outlet_id") == null)
            throw new UnknownEventFormatException(
                String.format("header.outlet_id is required for eventId:%s", eventIdNode.asText()), eventNode.toString());

        if(headerNode.get("version") == null)
            throw new UnknownEventFormatException(
                String.format("header.version is required for eventId:%s", eventIdNode.asText()), eventNode.toString());
    }
}
