package com.mekari.mokaaddons.common.webhook.moka.handler;

import com.mekari.mokaaddons.common.handler.VoidRequest;
import com.mekari.mokaaddons.common.webhook.moka.AbstractEvent;

public interface EventRequest<TEvent extends AbstractEvent> extends VoidRequest {
    TEvent getEvent();
}
