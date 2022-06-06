package com.mekari.mokaaddons.webhookconsumer.webhook.service.item.command.handlereceived;

import com.mekari.mokaaddons.common.webhook.moka.EventRequest;
import com.mekari.mokaaddons.webhookconsumer.webhook.service.item.event.MokaItemReceivedEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class HandleMokaItemReceivedRequest implements EventRequest {
    private MokaItemReceivedEvent event;
}
