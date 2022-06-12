package com.mekari.mokaaddons.webhookconsumer.event.item.processor.received;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;
import com.mekari.mokaaddons.common.webhook.moka.MokaEventHeader;
import com.mekari.mokaaddons.webhookconsumer.config.AppConstant;
import com.mekari.mokaaddons.webhookconsumer.event.item.MokaItemProcessedEvent;
import com.mekari.mokaaddons.webhookconsumer.event.item.MokaItemReceivedEvent;
import com.mekari.mokaaddons.webhookconsumer.event.item.MokaItemProcessedEvent.Body;
import com.mekari.mokaaddons.webhookconsumer.service.product.command.save.SaveProductRequest;

@Service
public class EventItemReceivedHandler extends AbstractVoidRequestHandler<EventItemReceivedRequest> {
    
    private AmqpTemplate amqpTemplate;
    private RequestHandlerManager handlerManager;

    public EventItemReceivedHandler(@Autowired RequestHandlerManager handlerManager, @Autowired AmqpTemplate amqpTemplate){
        this.handlerManager = handlerManager;
        this.amqpTemplate = amqpTemplate;
    }

    @Override
    protected void handleInternal(EventItemReceivedRequest request) throws Exception {
        saveEvent(request.getEvent());
        publishEvent(request.getEvent());
    }

    private void saveEvent(MokaItemReceivedEvent event) throws Exception {
        var header = event.getHeader();
        var body = event.getBody();

        logger.info(
                "eventId:%s-eventName:%s-dataId:%s creates or updates ProductMapping with  moka.item.processed",
                header.getEventId(), header.getEventName(), body.getId());

        var checkCreateProductRequest = SaveProductRequest.builder()
                .product(SaveProductRequest.Product.builder()
                        .mokaItemId(body.getId())
                        .name(body.getName())
                        .build())
                .build();
        handlerManager.handle(checkCreateProductRequest);
    }

    private void publishEvent(MokaItemReceivedEvent event) throws Exception {
        var header = event.getHeader();
        var data = event.getBody();
        logger.info(
                "eventId:%s-eventName:%s-dataId:%s publishes webHookEventProcessed with  moka.item.processed into Queue:%sQueue",
                header.getEventId(), header.getEventName(), data.getId(),
                AppConstant.ExchangeName.MOKA_EVENT_PROCESSED_EXCHANGE);

        var eventHeader = new MokaEventHeader(event.getHeader());
        eventHeader.setEventName("moka.item.processed");

        var eventBody = new Body(data.getId(), data.getDate());
        var itemProcessed = new MokaItemProcessedEvent(eventHeader, eventBody);
        amqpTemplate.convertAndSend(AppConstant.ExchangeName.MOKA_EVENT_PROCESSED_EXCHANGE, null, itemProcessed);
    }
}
