package com.mekari.mokaaddons.common.webhook.moka;

public abstract class AbstractEventBody<TData extends EventData> {
    public abstract TData getData();
}
