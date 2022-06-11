package com.mekari.mokaaddons.webhookconsumer.webhook.service.item.processor.handlereceived;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;
import com.mekari.mokaaddons.common.messaging.Publisher;
import com.mekari.mokaaddons.common.webhook.moka.MokaEventHeader;
import com.mekari.mokaaddons.webhookconsumer.config.AppConstant;
import com.mekari.mokaaddons.webhookconsumer.service.product.command.save.SaveProductRequest;
import com.mekari.mokaaddons.webhookconsumer.webhook.service.item.event.MokaItemProcessedEvent;
import com.mekari.mokaaddons.webhookconsumer.webhook.service.item.event.MokaItemProcessedEvent.Body;
import com.mekari.mokaaddons.webhookconsumer.webhook.service.item.event.MokaItemProcessedEvent.Item;
import com.mekari.mokaaddons.webhookconsumer.webhook.service.item.event.MokaItemReceivedEvent;

@Service
public class MokaItemReceivedHandler extends AbstractVoidRequestHandler<HandleMokaItemReceivedRequest> {
    
    private Publisher publisher;
    private RequestHandlerManager handlerManager;

    public MokaItemReceivedHandler(@Autowired RequestHandlerManager handlerManager, @Autowired Publisher publisher){
        this.publisher = publisher;
        this.handlerManager = handlerManager;
    }

    @Override
    protected void handleInternal(HandleMokaItemReceivedRequest request) throws Exception {
        saveEvent(request.getEvent());
        publishEvent(request.getEvent());
    }

    private void saveEvent(MokaItemReceivedEvent event) throws Exception {
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

    private void publishEvent(MokaItemReceivedEvent event) throws Exception {
        var header = event.getHeader();
        var data = event.getBody().getData();
        logger.info(
                "eventId:%s-eventName:%s-dataId:%s publishes webHookEventProcessed with  moka.item.processed into Queue:%sQueue",
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
