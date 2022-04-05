package com.mekari.mokaaddons.api.controller;

import com.mekari.mokaaddons.api.service.webhook.handleevent.HandleEventRequest;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebhookController {
    private @Autowired  RequestHandlerManager requestManager;
    private static final String SOURCE_NAME = WebhookController.class.getName();
    private static final Logger LOGGER = LogManager.getFormatterLogger(WebhookController.class);

    // it respresents the api endpoint which will be called by partner.
    public void handle(String message) throws Exception{
        LOGGER.debug("receice webhook message, with payload:%s", message);
        var request = HandleEventRequest.builder()
                        .json(message)
                        .sourceName(SOURCE_NAME)
                        .build();
        requestManager.handle(request);
    }
}