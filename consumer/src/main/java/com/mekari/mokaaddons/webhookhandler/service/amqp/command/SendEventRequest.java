package com.mekari.mokaaddons.webhookhandler.service.amqp.command;

import com.mekari.mokaaddons.webhookhandler.common.service.AbstractVoidRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SendEventRequest extends AbstractVoidRequest {
    private String exchange;
    private String routingKey;
    private Object message;
}
