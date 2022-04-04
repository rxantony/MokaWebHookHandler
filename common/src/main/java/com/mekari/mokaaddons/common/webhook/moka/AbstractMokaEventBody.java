package com.mekari.mokaaddons.common.webhook.moka;

public abstract class AbstractMokaEventBody<TData extends MokaEventData> {
    public abstract TData getData();
}
