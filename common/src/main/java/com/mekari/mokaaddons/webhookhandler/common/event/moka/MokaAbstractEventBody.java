package com.mekari.mokaaddons.webhookhandler.common.event.moka;

public abstract class MokaAbstractEventBody<TData extends MokaEventData> {
    public abstract TData getData();
}
