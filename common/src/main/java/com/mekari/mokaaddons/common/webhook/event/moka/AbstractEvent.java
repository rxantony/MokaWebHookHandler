package com.mekari.mokaaddons.common.webhook.event.moka;

import com.mekari.mokaaddons.common.webhook.event.Event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractEvent implements Event {
    private MokaEventHeader header;
    
    public abstract EventBody getBody();
}
