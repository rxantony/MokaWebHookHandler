package com.mekari.mokaaddons.webhookconsumer.service.webhook.mokaItemreceived;

import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;
import com.mekari.mokaaddons.common.publisher.Publisher;
import com.mekari.mokaaddons.common.util.DateUtil;
import com.mekari.mokaaddons.common.webhook.moka.MokaEventHeader;
import com.mekari.mokaaddons.webhookconsumer.config.AppConstant;
import com.mekari.mokaaddons.webhookconsumer.service.product.createproduct.CreateProductRequest;
import com.mekari.mokaaddons.webhookconsumer.service.product.productexists.ProductExistsRequest;
import com.mekari.mokaaddons.webhookconsumer.service.product.updateroduct.UpdateProductRequest;
import com.mekari.mokaaddons.webhookconsumer.service.webhook.mokaItemprocessed.MokaItemProcessedEvent;
import com.mekari.mokaaddons.webhookconsumer.service.webhook.mokaItemprocessed.MokaItemProcessedEvent.Body;
import com.mekari.mokaaddons.webhookconsumer.service.webhook.mokaItemprocessed.MokaItemProcessedEvent.Item;

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
        eventHeader.setEventName("moka.item.proc essed");

        var item = new Item(data.getId(), data.getDate());
        var eventBody = new Body(item);
        var itemProcessed = new MokaItemProcessedEvent(eventHeader, eventBody);
        publisher.publish(AppConstant.ExchangeName.MOKA_EVENT_PROCESSED_EXCHANGE, itemProcessed);
    }
}
