package com.mekari.mokaaddons.webhookhandler.command;

import com.mekari.mokaaddons.webhookhandler.common.command.AbstractCommand;
import com.mekari.mokaaddons.webhookhandler.common.event.moka.MokaEventHeader;
import com.mekari.mokaaddons.webhookhandler.common.service.RequestHandlerManager;
import com.mekari.mokaaddons.webhookhandler.common.util.DateUtil;
import com.mekari.mokaaddons.webhookhandler.config.AppConstant;
import com.mekari.mokaaddons.webhookhandler.event.MokaItemProcessed;
import com.mekari.mokaaddons.webhookhandler.event.MokaItemProcessed.Body;
import com.mekari.mokaaddons.webhookhandler.event.MokaItemProcessed.Item;
import com.mekari.mokaaddons.webhookhandler.event.MokaItemReceived;
import com.mekari.mokaaddons.webhookhandler.service.amqp.command.SendEventRequest;
import com.mekari.mokaaddons.webhookhandler.service.product.command.addProduct.AddProductRequest;
import com.mekari.mokaaddons.webhookhandler.service.product.command.updateProduct.UpdateProductRequest;
import com.mekari.mokaaddons.webhookhandler.service.product.query.productExists.ProductExistsRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component()
public class CommandMokaItemReceived extends AbstractCommand<MokaItemReceived> {
    private @Autowired RequestHandlerManager manager;

    public CommandMokaItemReceived() {
        super(MokaItemReceived.class);
    }

    @Override
    protected void executeInternal(MokaItemReceived event) throws Exception {
        saveEvent(event);
        publishEvent(event);
    }

    private void saveEvent(MokaItemReceived event) throws Exception {
        var header = event.getHeader();
        var data = event.getBody().getData();
        var productExists = manager.handle(ProductExistsRequest.builder().id(data.getId()).build());
        
        if (!productExists) {
            logger.info("eventId:%s-eventName:%s-dataId:%s inserts a new moka item",
            header.getEventId(), header.getEventName(), data.getId());
            var request = AddProductRequest.builder()
                            .id(data.getId())
                            .name(data.getName())
                            .description(data.getDescription())
                            .createdAt(DateUtil.now())
                            .build();
            manager.handle(request);
        } else {
            logger.info("eventId:%s-eventName:%s-dataId:%s updates a moka item",
            header.getEventId(), header.getEventName(), data.getId());
            var request = UpdateProductRequest.builder()
                            .id(data.getId())
                            .name(data.getName())
                            .description(data.getDescription())
                            .updatedAt(DateUtil.now())
                            .build();
            manager.handle(request);
        }
    }

    private void publishEvent(MokaItemReceived event) throws Exception{
        var header = event.getHeader();
        var data = event.getBody().getData();
        
        logger.info(
                "eventId:%s-eventName:%s-dataId:%s publishes webHookEventProcessedcwith  moka.item.processed into Queue:%sQueue",
                header.getEventId(), header.getEventName(), data.getId(),
                AppConstant.ExchangeName.MOKA_EVENT_PROCESSED_EXCHANGE);

        var eventHeader = (MokaEventHeader) event.getHeader().clone();
        eventHeader.setEventName("moka.item.processed");
        
        var eventBody = new Body(new Item(event.getBody().getData().getId(), event.getBody().getData().getDate()));
        var itemProcessed = new MokaItemProcessed(eventHeader,eventBody);
        var request = SendEventRequest.builder()
                        .exchange(AppConstant.ExchangeName.MOKA_EVENT_PROCESSED_EXCHANGE)
                        .routingKey(null)
                        .message(itemProcessed)
                        .build();

        manager.handle(request);
    }
}
