package com.mekari.mokaaddons.api.webhook.service.item.command.save;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.api.service.product.command.save.SaveProductRequest;
import com.mekari.mokaaddons.api.webhook.service.item.event.MokaItemReceivedEvent;
import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveItemHandler extends AbstractVoidRequestHandler<SaveItemRequest> {

    private @Autowired ObjectMapper mapper;
    private @Autowired RequestHandlerManager handlerManager;

    @Override
    protected void handleInternal(SaveItemRequest request) throws Exception {
        var event = mapper.readValue(request.getJson(), MokaItemReceivedEvent.class);
        var header = event.getHeader();
        var data = event.getBody().getData();

        logger.info(
                "eventId:%s-eventName:%s-dataId:%s creates or updates ProductMapping with  moka.item.processed",
                header.getEventId(), header.getEventName(), data.getId());

        var checkCreateProductRequest = SaveProductRequest.builder()
                .product(SaveProductRequest.Product.builder()
                        .mokaItemId(data.getId())
                        .name(data.getName())
                        .build())
                .build();
        handlerManager.handle(checkCreateProductRequest);
    }
}
