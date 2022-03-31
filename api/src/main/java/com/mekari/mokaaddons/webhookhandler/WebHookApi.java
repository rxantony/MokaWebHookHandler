package com.mekari.mokaaddons.webhookhandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.mekari.mokaaddons.webhookhandler.common.command.CommandInvoker;
import com.mekari.mokaaddons.webhookhandler.common.command.CommandInvokerException;
import com.mekari.mokaaddons.webhookhandler.common.handler.RequestHandlerManager;
import com.mekari.mokaaddons.webhookhandler.common.storage.DeadLetterStorage;
import com.mekari.mokaaddons.webhookhandler.common.util.BuilderUtil;
import com.mekari.mokaaddons.webhookhandler.common.util.DateUtil;
import com.mekari.mokaaddons.webhookhandler.query.getUser.Request;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebHookApi {
    private @Autowired CommandInvoker invoker;
    private @Autowired DeadLetterStorage deadLetterStorage;
    private @Autowired RequestHandlerManager manager;
    private static final String SOURCE_NAME = WebHookApi.class.getName();
    private static final Logger LOGGER = LogManager.getFormatterLogger(WebHookApi.class);

    // it respresents the api endpoint which will be called by partner.
    public void handle(String message) throws Exception{
        LOGGER.debug("receice webhook message, with payload:%s", message);
        try{
            var user = manager.handle(Request.builder().id(1).build());
            LOGGER.debug(user);
            invoker.invoke(message);
        }
        catch(Exception ex){
            JsonNode msgNode = null;
            if(ex instanceof CommandInvokerException )
                msgNode = ((CommandInvokerException)ex).eventNode;

            try{
                var builder = BuilderUtil.createDeadLetterStorageItemBuilder(msgNode)
                    .payload(message)
                    .source(SOURCE_NAME)
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