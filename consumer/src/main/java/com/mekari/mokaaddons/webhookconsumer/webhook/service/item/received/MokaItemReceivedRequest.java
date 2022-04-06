package com.mekari.mokaaddons.webhookconsumer.webhook.service.item.received;

import com.mekari.mokaaddons.common.webhook.moka.EventRequest;
import com.mekari.mokaaddons.webhookconsumer.webhook.event.MokaItemReceivedEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MokaItemReceivedRequest implements EventRequest {
    private MokaItemReceivedEvent event;
}