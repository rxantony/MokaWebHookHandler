package com.mekari.mokaaddons.api.webhook.service.item.save;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.api.service.product.checkcreate.CheckCreateProductRequest;
import com.mekari.mokaaddons.api.webhook.event.MokaItemReceivedEvent;
import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveItemHandler extends AbstractVoidRequestHandler<SaveItemRequest>{

    private @Autowired ObjectMapper mapper;
    private @Autowired RequestHandlerManager requestManager;

    @Override
    protected void handleInternal(SaveItemRequest request) throws Exception {
        var event = mapper.readValue(request.getJson(), MokaItemReceivedEvent.class);
        var data = event.getBody().getData();
        var checkCreateProductRequest = CheckCreateProductRequest.builder()
                                    .product(CheckCreateProductRequest.Product.builder()
                                                .mokaId(data.getId())
                                                .name(data.getName())
                                                .build())
                                    .build();
        requestManager.handle(checkCreateProductRequest);
    }

}
