package com.mekari.mokaaddons.common.webhook.moka;

import com.mekari.mokaaddons.common.webhook.Event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractEvent implements Event {
    private MokaEventHeader header;
    
    public abstract EventBody<?> getBody();
}
