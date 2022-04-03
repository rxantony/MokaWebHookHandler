package com.mekari.mokaaddons.common.webhook.moka;

public abstract class MokaAbstractEventBody<TData extends MokaEventData> {
    public abstract TData getData();
}
