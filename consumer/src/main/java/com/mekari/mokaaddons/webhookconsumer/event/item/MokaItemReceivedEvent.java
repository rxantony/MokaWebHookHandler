package com.mekari.mokaaddons.webhookconsumer.event.item;

import java.time.OffsetDateTime;

import com.mekari.mokaaddons.common.webhook.event.moka.AbstractEvent;
import com.mekari.mokaaddons.common.webhook.event.moka.EventBody;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MokaItemReceivedEvent extends AbstractEvent {

    private Body body;

    @Getter
    @Setter
    public static class Body implements EventBody {
        private String id;
        private String name;
        private OffsetDateTime date;
        private String description;
    }
}