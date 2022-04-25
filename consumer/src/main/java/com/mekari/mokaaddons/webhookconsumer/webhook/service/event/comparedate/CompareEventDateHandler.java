package com.mekari.mokaaddons.webhookconsumer.webhook.service.event.comparedate;

import com.mekari.mokaaddons.common.handler.RequestHandler;
import com.mekari.mokaaddons.common.webhook.EventSourceStorage;
import com.mekari.mokaaddons.common.webhook.moka.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompareEventDateHandler implements RequestHandler<CompareEventDateRequest, CompareEventDateResult> {

    private @Autowired EventSourceStorage storage;
    
    @Override
    public CompareEventDateResult handle(CompareEventDateRequest request) throws Exception {

        var header = request.getEvent().getHeader();
        var data = request.getEvent().getBody().getData();
        var lastEventDate = storage.getLastEventDate(data.getId().toString());

        if(lastEventDate.isEmpty())
            throw Util.eventNotFoundInEventSource(request.getEvent());

        return CompareEventDateResult.builder()
                .dataId(data.getId().toString())
                .eventDate(header.getTimestamp())
                .lastEventDate(lastEventDate.get())
                .isEqualsWithLastEventDate(header.getTimestamp().equals(lastEventDate.get()))
                .build();
    }
    
}
