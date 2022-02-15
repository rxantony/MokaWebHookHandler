package com.mekari.mokaaddons.webhookhandler.common.event;

public abstract class AbstractEventBody<TData extends EventData> implements EventBody {
    public abstract TData getData();
}
