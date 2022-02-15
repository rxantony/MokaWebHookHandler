package com.mekari.mokaaddons.webhookhandler.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mekari.mokaaddons.webhookhandler.common.event.AbstractEventBody;
import com.mekari.mokaaddons.webhookhandler.common.event.AbstractMokaEvent;
import com.mekari.mokaaddons.webhookhandler.common.event.EventData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MokaItemReceived extends AbstractMokaEvent<MokaItemReceived.Item> {

    private Body body;

    @Getter
    @Setter
    public static class Item extends EventData {
        private String name;
        private String description;
    }

    @Getter
    @Setter
    public static class Body extends AbstractEventBody<Item> {
        @JsonProperty("item")
        private Item data;
    }
}