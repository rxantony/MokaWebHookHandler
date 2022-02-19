package com.mekari.mokaaddons.webhookhandler.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.mekari.mokaaddons.webhookhandler.common.storage.DeadLetterStorage.Item.ItemBuilder;

public class JsonNodeUtil {
    private JsonNodeUtil(){}

    public static void fillEventIdAndName(ItemBuilder builder, JsonNode eventNode){  
        if(eventNode == null)
            return;      
        var headerNode = eventNode.get("header");
        if (headerNode != null) {
            var eventIdNode = headerNode.get("event_id");
            if (eventIdNode != null)
                builder.eventId(eventIdNode.asText());
        }
    }
}
