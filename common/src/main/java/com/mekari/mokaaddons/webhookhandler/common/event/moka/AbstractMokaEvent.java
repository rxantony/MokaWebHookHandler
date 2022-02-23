package com.mekari.mokaaddons.webhookhandler.common.event.moka;

import com.mekari.mokaaddons.webhookhandler.common.event.AbstractEvent;
import com.mekari.mokaaddons.webhookhandler.common.event.EventData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractMokaEvent<TData extends EventData> extends AbstractEvent<TData> {
    private MokaEventHeader header;
}
