package com.mekari.mokaaddons.webhookhandler.event;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mekari.mokaaddons.webhookhandler.common.event.moka.AbstractMokaEvent;
import com.mekari.mokaaddons.webhookhandler.common.event.moka.MokaAbstractEventBody;
import com.mekari.mokaaddons.webhookhandler.common.event.moka.MokaEventData;
import com.mekari.mokaaddons.webhookhandler.common.event.moka.MokaEventHeader;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MokaItemProcessed extends AbstractMokaEvent {

    private Body body;

    public MokaItemProcessed(MokaEventHeader header, Body body){
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
    public static class Body extends MokaAbstractEventBody<Item> {

        @JsonProperty("item")
        private Item data;
    }
}
