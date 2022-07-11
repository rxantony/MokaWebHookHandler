package com.mekari.mokaaddons.api.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.mekari.mokaaddons.api.webhook.event.processor.savepublish.SavePublishEventRequest;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebhookController {
    private final RequestHandlerManager handlerManager;
    private static final Logger logger = LogManager.getFormatterLogger(WebhookController.class);

    // sample, it respresents the api endpoint which will be called by partner.
    public void handle(String message) throws Exception {
        logger.debug("receice webhook message, with payload:%s", message);

        var request = SavePublishEventRequest.builder()
                .json(message)
                .sourceName(WebhookController.class.getName())
                .build();

        handlerManager.handle(request);
    }
}