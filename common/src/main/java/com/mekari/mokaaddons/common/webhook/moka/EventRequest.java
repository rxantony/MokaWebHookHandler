package com.mekari.mokaaddons.common.webhook.moka;

import com.mekari.mokaaddons.common.handler.VoidRequest;

public interface EventRequest extends VoidRequest {
    AbstractEvent getEvent();
}
