package com.mekari.mokaaddons.webhookhandler.event;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mekari.mokaaddons.webhookhandler.common.event.moka.AbstractMokaEvent;
import com.mekari.mokaaddons.webhookhandler.common.event.moka.MokaAbstractEventBody;
import com.mekari.mokaaddons.webhookhandler.common.event.moka.MokaEventData;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class MokaItemReceived extends AbstractMokaEvent {

    private Body body;

    @Getter
    @Setter
    public static class Item implements MokaEventData {
        private String id;
        private String name;
        private OffsetDateTime date;
        private String description;
    }

    @Getter
    @Setter
    public static class Body extends MokaAbstractEventBody<Item> {
        @JsonProperty("item")
        private Item data;
    }
}