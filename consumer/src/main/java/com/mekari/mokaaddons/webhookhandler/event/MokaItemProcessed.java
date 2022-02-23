package com.mekari.mokaaddons.webhookhandler.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lmax.disruptor.EventHandler;
import com.mekari.mokaaddons.webhookhandler.common.event.AbstractEvent;
import com.mekari.mokaaddons.webhookhandler.common.event.AbstractEventBody;
import com.mekari.mokaaddons.webhookhandler.common.event.EventData;
import com.mekari.mokaaddons.webhookhandler.common.event.EventHeader;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MokaItemProcessed extends AbstractEvent<MokaItemProcessed.Item> {

    private EventHeader header;
    private Body body;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item implements EventData {
        private String id;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Body extends AbstractEventBody<Item> {

        @JsonProperty("item")
        private Item data;
    }
}
