package com.mekari.mokaaddons.webhookconsumer.service.webhook.mokapos.item.received;

import com.mekari.mokaaddons.common.webhook.moka.handler.EventRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MokaItemReceivedRequest implements EventRequest<MokaItemReceivedEvent> {
    private MokaItemReceivedEvent event;
}
