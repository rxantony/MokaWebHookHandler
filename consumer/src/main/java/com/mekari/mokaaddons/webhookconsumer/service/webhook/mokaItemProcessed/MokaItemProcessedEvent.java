package com.mekari.mokaaddons.webhookconsumer.service.webhook.mokaItemprocessed;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mekari.mokaaddons.common.webhook.moka.AbstractEvent;
import com.mekari.mokaaddons.common.webhook.moka.AbstractEventBody;
import com.mekari.mokaaddons.common.webhook.moka.EventData;
import com.mekari.mokaaddons.common.webhook.moka.MokaEventHeader;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MokaItemProcessedEvent extends AbstractEvent {

    private Body body;

    public MokaItemProcessedEvent(MokaEventHeader header, Body body){
        this.setHeader(header);
        this.setBody(body);
    }
    
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item implements EventData {
        private String id;
        private OffsetDateTime date;
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
