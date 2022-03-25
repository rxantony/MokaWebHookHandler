package com.mekari.mokaaddons.webhookhandler.common.event.moka;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mekari.mokaaddons.webhookhandler.common.event.AbstractEventBody;

public abstract class MokaAbstractEventBody<TData extends MokaEventData> extends AbstractEventBody<TData> {
    @Override
    @JsonIgnore()
    public String getId() {
        return getData().getId();
    }

    public abstract TData getData();
}
