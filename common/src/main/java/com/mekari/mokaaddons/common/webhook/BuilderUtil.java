package com.mekari.mokaaddons.common.webhook;

import com.fasterxml.jackson.databind.JsonNode;

public class BuilderUtil {
    private BuilderUtil(){}

    public static DeadLetterStorage.NewItem.NewItemBuilder createDeadLetterStorageItemBuilder(JsonNode eventNode){
        var builder = DeadLetterStorage.NewItem.builder();  
        if(eventNode == null)
            return builder;  

        var headerNode = eventNode.get("header");
        if (headerNode != null) {
            var eventIdNode = headerNode.get("event_id");
            if (eventIdNode != null)
                builder.eventId(eventIdNode.asText());
        }
        return builder;
    }
}
