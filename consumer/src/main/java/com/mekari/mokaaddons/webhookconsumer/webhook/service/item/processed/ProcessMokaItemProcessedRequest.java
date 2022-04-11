package com.mekari.mokaaddons.webhookconsumer.webhook.service.item.processed;

import com.mekari.mokaaddons.common.webhook.moka.EventRequest;
import com.mekari.mokaaddons.webhookconsumer.webhook.event.MokaItemProcessedEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProcessMokaItemProcessedRequest implements EventRequest {
    private MokaItemProcessedEvent event;
}