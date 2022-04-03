package com.mekari.mokaaddons.api.service.webhook.handleEvent;

import com.fasterxml.jackson.databind.JsonNode;
import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.util.DateUtil;
import com.mekari.mokaaddons.common.webhook.BuilderUtil;
import com.mekari.mokaaddons.common.webhook.CommandInvoker;
import com.mekari.mokaaddons.common.webhook.CommandInvokerException;
import com.mekari.mokaaddons.common.webhook.DeadLetterStorage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HandleEvent extends AbstractVoidRequestHandler<HandleEventRequest> {
    private @Autowired CommandInvoker invoker;
    private @Autowired DeadLetterStorage deadLetterStorage;
    private static final Logger LOGGER = LogManager.getFormatterLogger(HandleEvent.class);

    @Override
    protected void handleInternal(HandleEventRequest request) {
        try{
            invoker.invoke(request.getJson());
        }
        catch(Exception ex){
            JsonNode msgNode = null;
            if(ex instanceof CommandInvokerException )
                msgNode = ((CommandInvokerException)ex).eventNode;

            try{
                var builder = BuilderUtil.createDeadLetterStorageItemBuilder(msgNode)
                    .payload(request.getJson())
                    .source(request.getSourceName())
                    .reason(ex.toString())
                    .createdAt(DateUtil.now());
                deadLetterStorage.insert(builder.build());
            }
            // basically we can forward this exception into ApiExceptionHandler.
            catch(Exception iex){
                LOGGER.error(iex.toString());
            }           
        }
    }
    
}
