package com.mekari.mokaaddons.common.webhook.event.moka;

import com.mekari.mokaaddons.common.handler.VoidRequest;

public interface EventRequest extends VoidRequest {
    AbstractEvent getEvent();
}
