package com.mekari.mokaaddons.webhookhandler.common.event.moka;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractMokaEvent implements Event {
    private MokaEventHeader header;
    
    public abstract MokaAbstractEventBody<?> getBody();
}
