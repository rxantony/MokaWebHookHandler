package com.mekari.mokaaddons.webhookhandler.event;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mekari.mokaaddons.webhookhandler.common.event.AbstractEvent;
import com.mekari.mokaaddons.webhookhandler.common.event.AbstractEventBody;
import com.mekari.mokaaddons.webhookhandler.common.event.EventData;
import com.mekari.mokaaddons.webhookhandler.common.event.EventHeader;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MokaItemProcessed extends AbstractEvent<MokaItemProcessed.Item> {

    private Body body;

    public MokaItemProcessed(EventHeader header, Body body) {
        setHeader(header);
        setBody(body);
    }

    @NoArgsConstructor
    public static class Item extends EventData {
        public Item(String id, OffsetDateTime updatedAt) {
            super(id, updatedAt);
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Body extends AbstractEventBody<Item> {
        @JsonProperty("item")
        private Item data;
    }
}
