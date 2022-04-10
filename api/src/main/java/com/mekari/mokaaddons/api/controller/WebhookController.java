package com.mekari.mokaaddons.api.controller;

import java.util.Date;

import com.mekari.mokaaddons.api.service.product.manualysync.ManualProductSyncRequest;
import com.mekari.mokaaddons.api.webhook.service.event.savepublish.SavePublishEventRequest;
import com.mekari.mokaaddons.api.webhook.service.item.save.SaveItemRequest;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebhookController {
    private @Autowired  RequestHandlerManager handlerManager;
    private static final Logger LOGGER = LogManager.getFormatterLogger(WebhookController.class);

    // it respresents the api endpoint which will be called by partner.
    public void handle(String message) throws Exception{
        LOGGER.debug("receice webhook message, with payload:%s", message);
        
        var request = SavePublishEventRequest.builder()
                        .json(message)
                        .sourceName(WebhookController.class.getName())
                        .build();

        handlerManager.handle(request);
    }

    //omm Koji sample case.
    public void feedProduct(String message) throws Exception{
        var request = SaveItemRequest.builder()
                        .json(message)
                        .build();

        handlerManager.handle(request);
    }

    public void manualProductSync(Date from, Date to) throws Exception{
        var request = ManualProductSyncRequest.builder()
                        .from(from)
                        .to(to)
                        .build();

        handlerManager.handle(request);
    }

}