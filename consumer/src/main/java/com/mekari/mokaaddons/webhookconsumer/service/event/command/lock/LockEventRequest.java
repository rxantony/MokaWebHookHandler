package com.mekari.mokaaddons.webhookconsumer.service.event.command.lock;

import com.mekari.mokaaddons.common.handler.Request;
import com.mekari.mokaaddons.common.handler.Validateable;
import com.mekari.mokaaddons.common.webhook.event.moka.AbstractEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LockEventRequest implements Request<LockEventResult>, Validateable {
    private AbstractEvent event;

    @Override
    public void validate() throws Exception {
        if(event == null) throw new IllegalArgumentException("event must not be null");
    }
}
