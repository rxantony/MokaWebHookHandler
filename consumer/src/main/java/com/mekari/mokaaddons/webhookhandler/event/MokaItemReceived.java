package com.mekari.mokaaddons.webhookhandler.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mekari.mokaaddons.webhookhandler.common.event.AbstractEventBody;
import com.mekari.mokaaddons.webhookhandler.common.event.EventData;
import com.mekari.mokaaddons.webhookhandler.common.event.moka.AbstractMokaEvent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MokaItemReceived extends AbstractMokaEvent<MokaItemReceived.Item> {

    private Body body;

    @Getter
    @Setter
    public static class Item implements EventData {
        private String id;
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