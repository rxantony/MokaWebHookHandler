package com.mekari.mokaaddons.common.webhook.moka.handler.saveAndPublishEvent;

import com.mekari.mokaaddons.common.webhook.moka.AbstractMokaEvent;
import com.mekari.mokaaddons.common.webhook.moka.handler.MokaRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SaveAndPublishEventRequest implements MokaRequest<AbstractMokaEvent>{
    private AbstractMokaEvent event;
}
