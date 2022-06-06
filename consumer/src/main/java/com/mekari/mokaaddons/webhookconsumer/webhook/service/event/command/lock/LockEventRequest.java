package com.mekari.mokaaddons.webhookconsumer.webhook.service.event.command.lock;

import com.mekari.mokaaddons.common.handler.Request;
import com.mekari.mokaaddons.common.webhook.moka.AbstractEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LockEventRequest implements Request<LockEventResult> {
    private AbstractEvent event;
}
