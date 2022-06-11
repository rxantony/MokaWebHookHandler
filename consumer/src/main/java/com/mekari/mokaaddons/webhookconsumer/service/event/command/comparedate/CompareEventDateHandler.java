package com.mekari.mokaaddons.webhookconsumer.service.event.command.comparedate;

import com.mekari.mokaaddons.common.handler.RequestHandler;
import com.mekari.mokaaddons.common.webhook.EventSourceStorage;
import com.mekari.mokaaddons.common.webhook.moka.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompareEventDateHandler implements RequestHandler<CompareEventDateRequest, CompareEventDateResult> {

    private EventSourceStorage storage;
    
    public CompareEventDateHandler(@Autowired EventSourceStorage storage){
        this.storage = storage;
    }

    @Override
    public CompareEventDateResult handle(CompareEventDateRequest request) throws Exception {

        var header = request.getEvent().getHeader();
        var body = request.getEvent().getBody();
        var lastEventDate = storage.getLastEventDate(body.getId().toString());

        if(lastEventDate.isEmpty())
            throw Util.eventNotFoundInEventSource(request.getEvent());

        return CompareEventDateResult.builder()
                .dataId(body.getId().toString())
                .eventDate(header.getTimestamp())
                .lastEventDate(lastEventDate.get())
                .isEqualsWithLastEventDate(header.getTimestamp().equals(lastEventDate.get()))
                .build();
    }
    
}
