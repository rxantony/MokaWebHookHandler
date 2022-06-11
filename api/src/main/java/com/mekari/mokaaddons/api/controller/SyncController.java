package com.mekari.mokaaddons.api.controller;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mekari.mokaaddons.api.service.product.command.syncmanual.SyncProductManualRequest;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;

@Component
public class SyncController {
    private @Autowired  RequestHandlerManager handlerManager;
    private static final Logger logger = LogManager.getFormatterLogger(WebhookController.class);

    //sample case.
    public void manualProductSync(Date from, Date to) throws Exception{
        var request = SyncProductManualRequest.builder()
                        .from(from)
                        .to(to)
                        .build();

        handlerManager.handle(request);
    }

}