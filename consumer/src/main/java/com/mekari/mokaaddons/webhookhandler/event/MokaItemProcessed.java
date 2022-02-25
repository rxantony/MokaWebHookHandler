package com.mekari.mokaaddons.webhookhandler.event;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mekari.mokaaddons.webhookhandler.common.event.AbstractEvent;
import com.mekari.mokaaddons.webhookhandler.common.event.AbstractEventBody;
import com.mekari.mokaaddons.webhookhandler.common.event.EventData;
import com.mekari.mokaaddons.webhookhandler.common.event.EventHeader;
import com.mekari.mokaaddons.webhookhandler.common.event.moka.MokaEventData;

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

    @Override
    @JsonIgnore()
    public OffsetDateTime getDate() {
        return getBody().getData().getDate();
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
    public static class Body extends AbstractEventBody<Item> {

        @JsonProperty("item")
        private Item data;
    }
}
