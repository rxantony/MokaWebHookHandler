package com.mekari.mokaaddons.webhookhandler.common.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractEvent<TData extends EventData> implements Event {
    private EventHeader header;
    public abstract  AbstractEventBody<TData> getBody();
}
