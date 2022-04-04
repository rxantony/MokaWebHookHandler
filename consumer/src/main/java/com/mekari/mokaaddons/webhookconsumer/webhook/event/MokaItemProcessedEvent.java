package com.mekari.mokaaddons.webhookconsumer.webhook.event;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mekari.mokaaddons.common.webhook.moka.AbstractMokaEvent;
import com.mekari.mokaaddons.common.webhook.moka.AbstractMokaEventBody;
import com.mekari.mokaaddons.common.webhook.moka.MokaEventData;
import com.mekari.mokaaddons.common.webhook.moka.MokaEventHeader;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MokaItemProcessedEvent extends AbstractMokaEvent {

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
    public static class Item implements MokaEventData {
        private String id;
        private OffsetDateTime date;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Body extends AbstractMokaEventBody<Item> {

        @JsonProperty("item")
        private Item data;
    }
}
