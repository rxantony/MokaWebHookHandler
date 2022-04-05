package com.mekari.mokaaddons.common.webhook.moka.service.savethenpublishevent;

import com.mekari.mokaaddons.common.webhook.moka.AbstractEvent;
import com.mekari.mokaaddons.common.webhook.moka.service.EventRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SaveThenPublishEventRequest implements EventRequest<AbstractEvent>{
    private AbstractEvent event;
}
