package com.mekari.mokaaddons.webhookconsumer.webhook.service.item.received;

import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;
import com.mekari.mokaaddons.common.infrastructure.messaging.Publisher;
import com.mekari.mokaaddons.common.webhook.moka.MokaEventHeader;
import com.mekari.mokaaddons.webhookconsumer.config.AppConstant;
import com.mekari.mokaaddons.webhookconsumer.service.product.checkcreateupdate.CreateUpdateProductRequest;
import com.mekari.mokaaddons.webhookconsumer.webhook.event.*;
import com.mekari.mokaaddons.webhookconsumer.webhook.event.MokaItemProcessedEvent.Body;
import com.mekari.mokaaddons.webhookconsumer.webhook.event.MokaItemProcessedEvent.Item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MokaItemReceivedHandler extends AbstractVoidRequestHandler<MokaItemReceivedRequest> {

    private @Autowired Publisher publisher;
    private @Autowired RequestHandlerManager handlerManager;

    @Override
    protected void handleInternal(MokaItemReceivedRequest request) throws Exception {
        saveEvent(request.getEvent());
        publishEvent(request.getEvent());
    }

    private void saveEvent(MokaItemReceivedEvent event) throws Exception {
        var data = event.getBody().getData();
        var checkCreateProductRequest = CreateUpdateProductRequest.builder()
                                            .product(CreateUpdateProductRequest.Product.builder()
                                                        .mokaItemId(data.getId())
                                                        .name(data.getName())
                                                        .build())
                                            .build();
        handlerManager.handle(checkCreateProductRequest);
    }

    private void publishEvent(MokaItemReceivedEvent event) throws Exception {
        var header = event.getHeader();
        var data = event.getBody().getData();
        logger.info(
                "eventId:%s-eventName:%s-dataId:%s publishes webHookEventProcessedcwith  moka.item.processed into Queue:%sQueue",
                header.getEventId(), header.getEventName(), data.getId(),
                AppConstant.ExchangeName.MOKA_EVENT_PROCESSED_EXCHANGE);

        var eventHeader = (MokaEventHeader) event.getHeader().clone();
        eventHeader.setEventName("moka.item.processed");

        var item = new Item(data.getId(), data.getDate());
        var eventBody = new Body(item);
        var itemProcessed = new MokaItemProcessedEvent(eventHeader, eventBody);
        publisher.publish(AppConstant.ExchangeName.MOKA_EVENT_PROCESSED_EXCHANGE, itemProcessed);
    }
}
