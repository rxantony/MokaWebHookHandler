package com.mekari.mokaaddons.webhookconsumer.service.webhook.mokaItemProcessed;

import com.mekari.mokaaddons.common.webhook.moka.handler.MokaEventRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MokaItemProcessedRequest implements MokaEventRequest<MokaItemProcessedEvent> {
    private MokaItemProcessedEvent event;
}
