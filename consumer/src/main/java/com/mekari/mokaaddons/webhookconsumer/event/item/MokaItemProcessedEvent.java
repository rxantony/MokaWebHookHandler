package com.mekari.mokaaddons.webhookconsumer.event.item;

import java.time.OffsetDateTime;

import com.mekari.mokaaddons.common.webhook.moka.AbstractEvent;
import com.mekari.mokaaddons.common.webhook.moka.EventBody;
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
    public static class Body implements EventBody {
        private String id;
        private OffsetDateTime date;
    }
}
