package com.mekari.mokaaddons.webhookconsumer.webhook.service.item.processor.handleprocessed;

import com.mekari.mokaaddons.common.handler.Validateable;
import com.mekari.mokaaddons.common.webhook.moka.EventRequest;
import com.mekari.mokaaddons.webhookconsumer.webhook.service.item.event.MokaItemProcessedEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class HandleMokaItemProcessedRequest implements EventRequest, Validateable {
    private MokaItemProcessedEvent event;

    @Override
    public void validate() throws Exception {
        if(event == null) throw new IllegalArgumentException("event must not be null");
    }
}
