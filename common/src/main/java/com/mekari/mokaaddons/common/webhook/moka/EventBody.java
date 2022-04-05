package com.mekari.mokaaddons.common.webhook.moka;

public interface EventBody<TData extends EventData> {
    TData getData();
}
