package com.mekari.mokaaddons.api.service.product.command.sync;

import java.io.FileInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.api.service.product.command.save.SaveProductRequest;
import com.mekari.mokaaddons.api.webhook.event.MokaItemReceivedEvent;
import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SyncProductHandler extends AbstractVoidRequestHandler<SyncProductRequest> {

    private final ObjectMapper mapper;
    private final RequestHandlerManager handlerManager;

    @Override
    protected void handleInternal(SyncProductRequest request) throws Exception {

        logger.debug("get moka items from moka.api from:%s to:%s", request.getFrom(), request.getTo());

        var response = justForSampleGetMokaItemsFromMokaApi();
        var mokaItems = new MokaItemReceivedEvent[]{};
        mokaItems = mapper.readValue(response, mokaItems.getClass());

        var requestBuilder = SaveProductRequest.builder();
        for(var mokaItem : mokaItems){
            var body  = mokaItem.getBody();
            requestBuilder.product(SaveProductRequest.Product.builder()
                        .mokaItemId(body.getId())
                        .name(body.getName())
                        .build());
        }

        handlerManager.handle(requestBuilder.build());
    }

    private String justForSampleGetMokaItemsFromMokaApi() throws Exception{
        var file = ResourceUtils.getFile("classpath:item_event_varies.json");
        try (var in = new FileInputStream(file);) {
            var writer = new StringWriter();
            IOUtils.copy(in, writer, StandardCharsets.UTF_8);
            return writer.toString();
        }
    }
    
}
