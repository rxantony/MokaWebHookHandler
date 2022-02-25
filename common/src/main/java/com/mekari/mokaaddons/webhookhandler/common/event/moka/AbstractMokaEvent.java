package com.mekari.mokaaddons.webhookhandler.common.event.moka;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mekari.mokaaddons.webhookhandler.common.event.AbstractEvent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractMokaEvent<TData extends MokaEventData> extends AbstractEvent<TData> {
    private MokaEventHeader header;

    @Override
    @JsonIgnore()
    public OffsetDateTime getDate() {
        return getBody().getData().getDate();
    }
}
