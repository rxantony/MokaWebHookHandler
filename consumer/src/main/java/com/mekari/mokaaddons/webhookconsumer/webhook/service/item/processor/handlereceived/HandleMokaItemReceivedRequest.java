package com.mekari.mokaaddons.webhookconsumer.webhook.service.item.processor.handlereceived;

import com.mekari.mokaaddons.common.handler.Validateable;
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
public class HandleMokaItemReceivedRequest implements EventRequest, Validateable {
    private MokaItemReceivedEvent event;

    @Override
    public void validate() throws Exception {
        if(event == null) throw new IllegalArgumentException("event must not be null");
    }
}
