package com.mekari.mokaaddons.common.webhook.moka;

import com.mekari.mokaaddons.common.webhook.Event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractMokaEvent implements Event {
    private MokaEventHeader header;
    
    public abstract AbstractMokaEventBody<?> getBody();
}
