package com.mekari.mokaaddons.webhookconsumer.event.item.consumer.processed;

import com.mekari.mokaaddons.common.handler.Validateable;
import com.mekari.mokaaddons.common.webhook.event.moka.EventRequest;
import com.mekari.mokaaddons.webhookconsumer.event.item.MokaItemProcessedEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EventItemProcessedRequest implements EventRequest, Validateable {
    private MokaItemProcessedEvent event;

    @Override
    public void validate() throws Exception {
        if(event == null) throw new IllegalArgumentException("event must not be null");
    }
}
