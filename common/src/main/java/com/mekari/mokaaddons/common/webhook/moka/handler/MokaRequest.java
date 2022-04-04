package com.mekari.mokaaddons.common.webhook.moka.handler;

import com.mekari.mokaaddons.common.handler.VoidRequest;
import com.mekari.mokaaddons.common.webhook.moka.AbstractMokaEvent;

public interface MokaRequest<TEvent extends AbstractMokaEvent> extends VoidRequest {
    TEvent getEvent();
}
