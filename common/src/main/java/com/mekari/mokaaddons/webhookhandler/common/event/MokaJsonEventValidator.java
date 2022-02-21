package com.mekari.mokaaddons.webhookhandler.common.event;

import com.fasterxml.jackson.databind.JsonNode;

public class MokaJsonEventValidator extends DefaultJsonEventValidator{

    @Override
    public void validate(JsonNode eventNode) throws UnknownEventFormatException {
        super.validate(eventNode);
        var headerNode = eventNode.get("header");
        if(headerNode.get("outlet_id") == null)
            throw new UnknownEventFormatException("header.outlet_id is required", eventNode.toString());
        if(headerNode.get("version") == null)
            throw new UnknownEventFormatException("header.version is required", eventNode.toString());
        if(headerNode.get("timestamp") == null)
            throw new UnknownEventFormatException("header.timestamp is required", eventNode.toString());
    }
    
}
