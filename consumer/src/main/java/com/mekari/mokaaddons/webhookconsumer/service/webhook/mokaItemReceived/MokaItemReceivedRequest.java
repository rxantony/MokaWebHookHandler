package com.mekari.mokaaddons.webhookconsumer.service.webhook.mokaItemReceived;

import com.mekari.mokaaddons.common.handler.VoidRequest;
import com.mekari.mokaaddons.common.webhook.moka.handler.MokaEventRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MokaItemReceivedRequest implements MokaEventRequest<MokaItemReceivedEvent> {
    private MokaItemReceivedEvent event;
}
