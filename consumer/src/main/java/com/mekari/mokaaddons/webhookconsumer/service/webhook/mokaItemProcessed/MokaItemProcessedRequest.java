package com.mekari.mokaaddons.webhookconsumer.service.webhook.mokaItemprocessed;

import com.mekari.mokaaddons.common.webhook.moka.handler.EventRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MokaItemProcessedRequest implements EventRequest<MokaItemProcessedEvent> {
    private MokaItemProcessedEvent event;
}
