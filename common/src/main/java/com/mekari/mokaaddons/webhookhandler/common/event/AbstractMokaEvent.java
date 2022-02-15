package com.mekari.mokaaddons.webhookhandler.common.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractMokaEvent<TData extends EventData> implements Event {
    private MokaEventHeader header;

    public abstract AbstractEventBody<TData> getBody();
}
