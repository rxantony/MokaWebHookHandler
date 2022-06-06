package com.mekari.mokaaddons.webhookconsumer.webhook.service.item.command.handleprocessed;

import com.mekari.mokaaddons.common.webhook.moka.EventRequest;
import com.mekari.mokaaddons.webhookconsumer.webhook.service.item.event.MokaItemProcessedEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class HandleMokaItemProcessedRequest implements EventRequest {
    private MokaItemProcessedEvent event;
}
