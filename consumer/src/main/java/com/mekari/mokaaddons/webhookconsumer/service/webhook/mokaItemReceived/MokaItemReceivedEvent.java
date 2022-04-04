package com.mekari.mokaaddons.webhookconsumer.service.webhook.mokaItemReceived;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mekari.mokaaddons.common.webhook.moka.AbstractMokaEvent;
import com.mekari.mokaaddons.common.webhook.moka.AbstractMokaEventBody;
import com.mekari.mokaaddons.common.webhook.moka.MokaEventData;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class MokaItemReceivedEvent extends AbstractMokaEvent {

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
    public static class Body extends AbstractMokaEventBody<Item> {
        @JsonProperty("item")
        private Item data;
    }
}