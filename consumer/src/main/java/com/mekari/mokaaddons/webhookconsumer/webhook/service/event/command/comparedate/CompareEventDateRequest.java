package com.mekari.mokaaddons.webhookconsumer.webhook.service.event.command.comparedate;

import com.mekari.mokaaddons.common.handler.Request;
import com.mekari.mokaaddons.common.handler.Validator;
import com.mekari.mokaaddons.common.webhook.moka.AbstractEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor 
public class CompareEventDateRequest implements Request<CompareEventDateResult>, Validator {
    private AbstractEvent event;

    @Override
    public void validate() throws Exception {
        if(event == null) throw new IllegalArgumentException("event must not be null");
    }
}
