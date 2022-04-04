package com.mekari.mokaaddons.webhookconsumer.service.webhook.mokaItemReceived;

import com.mekari.mokaaddons.common.handler.VoidRequest;
import com.mekari.mokaaddons.common.webhook.moka.handler.MokaRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MokaItemReceivedRequest implements MokaRequest<MokaItemReceivedEvent> {
    private MokaItemReceivedEvent event;
}
