package com.mekari.mokaaddons.webhookconsumer.webhook.service.event.comparedate;

import com.mekari.mokaaddons.common.handler.RequestHandler;
import com.mekari.mokaaddons.common.webhook.EventSourceStorage;
import com.mekari.mokaaddons.common.webhook.moka.EventSourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompareEventDateHandler implements RequestHandler<CompareEventDateRequest, CompareEventDateResult> {

    private @Autowired EventSourceStorage storage;
    
    @Override
    public CompareEventDateResult handle(CompareEventDateRequest request) throws Exception {

        var lastEventDate = storage.getLastEventDate(request.getDataId());
        if(lastEventDate.isEmpty())
            throw new EventSourceNotFoundException(String.format("event source with dataId:%s is not found", request.getDataId()), null);

        return CompareEventDateResult.builder()
                .dataId(request.getDataId())
                .eventDate(request.getEvenDate())
                .lastEventDate(lastEventDate.get())
                .isEqualsWithLastEventDate(request.getEvenDate().equals(lastEventDate.get()))
                .build();
    }
    
}
