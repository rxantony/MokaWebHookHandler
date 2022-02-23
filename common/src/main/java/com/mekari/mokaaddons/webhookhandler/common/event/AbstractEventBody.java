package com.mekari.mokaaddons.webhookhandler.common.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class AbstractEventBody <TData extends EventData> implements EventBody {
    @Override
    @JsonIgnore()
    public String getId() {
        return getData().getId();
    }

    public abstract TData getData();
}

