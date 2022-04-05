package com.mekari.mokaaddons.common.webhook.moka.handler.saveAndPublishEvent;

import com.mekari.mokaaddons.common.webhook.moka.AbstractEvent;
import com.mekari.mokaaddons.common.webhook.moka.handler.EventRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SaveAndPublishEventRequest implements EventRequest<AbstractEvent>{
    private AbstractEvent event;
}
