package com.mekari.mokaaddons.webhookconsumer.event.item.processor.received;

import com.mekari.mokaaddons.common.handler.Validateable;
import com.mekari.mokaaddons.common.webhook.moka.EventRequest;
import com.mekari.mokaaddons.webhookconsumer.event.item.MokaItemReceivedEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EventItemReceivedRequest implements EventRequest, Validateable {
    private MokaItemReceivedEvent event;

    @Override
    public void validate() throws Exception {
        if(event == null) throw new IllegalArgumentException("event must not be null");
    }
}
