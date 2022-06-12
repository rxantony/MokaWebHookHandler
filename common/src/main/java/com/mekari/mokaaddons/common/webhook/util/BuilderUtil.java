package com.mekari.mokaaddons.common.webhook.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.mekari.mokaaddons.common.webhook.persistence.storage.DeadLetterStorage;

public class BuilderUtil {
    
    private BuilderUtil(){}

    public static DeadLetterStorage.NewDeadLetter.Builder createDeadLetterStorageItemBuilder(JsonNode eventNode){
        var builder = DeadLetterStorage.NewDeadLetter.builder();  
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
