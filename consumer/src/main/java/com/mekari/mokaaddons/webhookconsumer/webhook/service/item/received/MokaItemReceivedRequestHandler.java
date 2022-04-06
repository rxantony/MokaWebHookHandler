package com.mekari.mokaaddons.webhookconsumer.webhook.service.item.received;

import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;
import com.mekari.mokaaddons.common.infrastructure.messaging.Publisher;
import com.mekari.mokaaddons.common.util.DateUtil;
import com.mekari.mokaaddons.common.webhook.moka.MokaEventHeader;
import com.mekari.mokaaddons.webhookconsumer.config.AppConstant;
import com.mekari.mokaaddons.webhookconsumer.service.product.create.CreateProductRequest;
import com.mekari.mokaaddons.webhookconsumer.service.product.exists.ProductExistsRequest;
import com.mekari.mokaaddons.webhookconsumer.service.product.update.UpdateProductRequest;
import com.mekari.mokaaddons.webhookconsumer.webhook.event.*;
import com.mekari.mokaaddons.webhookconsumer.webhook.event.MokaItemProcessedEvent.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MokaItemReceivedRequestHandler extends AbstractVoidRequestHandler<MokaItemReceivedRequest> {

    private @Autowired Publisher publisher;
    private @Autowired RequestHandlerManager requestManager;

    @Override
    protected void handleInternal(MokaItemReceivedRequest request) throws Exception {
        saveEvent(request.getEvent());
        publishEvent(request.getEvent());
    }

    private void saveEvent(MokaItemReceivedEvent event) throws Exception {
        var header = event.getHeader();
        var data = event.getBody().getData();
        var productExists = requestManager.handle(ProductExistsRequest.builder().id(data.getId()).build());

        if (!productExists) {
            logger.info("eventId:%s-eventName:%s-dataId:%s inserts a new moka item",
                    header.getEventId(), header.getEventName(), data.getId());
            var request = CreateProductRequest.builder()
                    .id(data.getId())
                    .name(data.getName())
                    .description(data.getDescription())
                    .createdAt(DateUtil.now())
                    .build();
            requestManager.handle(request);
        } else {
            logger.info("eventId:%s-eventName:%s-dataId:%s updates a moka item",
                    header.getEventId(), header.getEventName(), data.getId());
            var request = UpdateProductRequest.builder()
                    .id(data.getId())
                    .name(data.getName())
                    .description(data.getDescription())
                    .updatedAt(DateUtil.now())
                    .build();
            requestManager.handle(request);
        }
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
