package com.mekari.mokaaddons.webhookconsumer.webhook.service.email.send;

import com.mekari.mokaaddons.common.webhook.moka.AbstractEvent;
import com.mekari.mokaaddons.common.webhook.moka.EventRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SendEmailRequest implements EventRequest{
    private AbstractEvent event;
}
