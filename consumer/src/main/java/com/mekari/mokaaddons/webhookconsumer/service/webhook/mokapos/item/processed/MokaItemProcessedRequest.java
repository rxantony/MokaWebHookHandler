package com.mekari.mokaaddons.webhookconsumer.service.webhook.mokapos.item.processed;

import com.mekari.mokaaddons.common.webhook.moka.service.EventRequest;

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
