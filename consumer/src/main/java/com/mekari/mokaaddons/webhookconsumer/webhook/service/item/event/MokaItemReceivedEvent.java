package com.mekari.mokaaddons.webhookconsumer.webhook.service.item.event;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mekari.mokaaddons.common.webhook.moka.AbstractEvent;
import com.mekari.mokaaddons.common.webhook.moka.EventBody;
import com.mekari.mokaaddons.common.webhook.moka.EventData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MokaItemReceivedEvent extends AbstractEvent {

    private Body body;

    @Getter
    @Setter
    public static class Item implements EventData {
        private String id;
        private String name;
        private OffsetDateTime date;
        private String description;
    }

    @Getter
    @Setter
    public static class Body implements EventBody {

        @JsonProperty("item")
        private Item data;
    }
}