package com.mekari.mokaaddons.api.controller;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.mekari.mokaaddons.api.service.product.command.sync.SyncProductRequest;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SyncController {
    private final RequestHandlerManager handlerManager;

    // sample case.
    public void manualProductSync(Date from, Date to) throws Exception {
        var request = SyncProductRequest.builder()
                .from(from)
                .to(to)
                .build();

        handlerManager.handle(request);
    }

}