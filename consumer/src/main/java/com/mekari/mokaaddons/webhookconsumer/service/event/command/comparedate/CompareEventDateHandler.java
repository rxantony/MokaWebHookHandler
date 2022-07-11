package com.mekari.mokaaddons.webhookconsumer.service.event.command.comparedate;

import org.springframework.stereotype.Service;

import com.mekari.mokaaddons.common.handler.RequestHandler;
import com.mekari.mokaaddons.common.webhook.persistence.storage.EventSourceStorage;
import com.mekari.mokaaddons.common.webhook.util.ExceptionUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompareEventDateHandler implements RequestHandler<CompareEventDateRequest, CompareEventDateResult> {

    private final EventSourceStorage storage;

    @Override
    public CompareEventDateResult handle(CompareEventDateRequest request) throws Exception {

        var header = request.getEvent().getHeader();
        var body = request.getEvent().getBody();
        var lastEventDate = storage.getLastEventDate(body.getId().toString());

        if(lastEventDate.isEmpty())
            throw ExceptionUtil.eventNotFoundInEventSource(request.getEvent());

        return CompareEventDateResult.builder()
                .dataId(body.getId().toString())
                .eventDate(header.getTimestamp())
                .lastEventDate(lastEventDate.get())
                .isEqualsWithLastEventDate(header.getTimestamp().equals(lastEventDate.get()))
                .build();
    }
    
}
