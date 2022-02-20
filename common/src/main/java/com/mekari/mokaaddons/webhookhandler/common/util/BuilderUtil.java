package com.mekari.mokaaddons.webhookhandler.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.mekari.mokaaddons.webhookhandler.common.storage.DeadLetterStorage;

public class BuilderUtil {
    private BuilderUtil(){}

    public static DeadLetterStorage.Item.ItemBuilder createDeadLetterStorageItemBuilder(JsonNode eventNode){
        var builder = DeadLetterStorage.Item.builder();  
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
