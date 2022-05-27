package com.mekari.mokaaddons.api.service.product.syncmanual;

import java.io.FileInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.api.service.product.save.SaveProductRequest;
import com.mekari.mokaaddons.api.webhook.event.MokaItemReceivedEvent;
import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
public class SyncProductManualHandler extends AbstractVoidRequestHandler<SyncProductManualRequest> {

    private @Autowired ObjectMapper mapper;
    private @Autowired RequestHandlerManager handlerManager;

    public SyncProductManualHandler(@Autowired RequestHandlerManager handlerManager,
            @Autowired ObjectMapper mapper){
        this.handlerManager = handlerManager;
        this.mapper = mapper;
    }
    @Override
    protected void handleInternal(SyncProductManualRequest request) throws Exception {

        logger.debug("get moka items from moka.api from:%s to:%s", request.getFrom(), request.getTo());

        var response = justForSampleGetMokaItemsFromMokaApi();
        var mokaItems = new MokaItemReceivedEvent[]{};
        mokaItems = mapper.readValue(response, mokaItems.getClass());

        var requestBuilder = SaveProductRequest.builder();
        for(var mokaItem : mokaItems){
            var data  = mokaItem.getBody().getData();
            requestBuilder.product(SaveProductRequest.Product.builder()
                        .mokaItemId(data.getId())
                        .name(data.getName())
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
