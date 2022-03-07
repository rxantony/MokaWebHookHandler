package com.mekari.mokaaddons.webhookhandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.mekari.mokaaddons.webhookhandler.common.command.CommandInvoker;
import com.mekari.mokaaddons.webhookhandler.common.command.JsonCommandInvokerException;
import com.mekari.mokaaddons.webhookhandler.common.storage.DeadLetterStorage;
import com.mekari.mokaaddons.webhookhandler.common.util.BuilderUtil;
import com.mekari.mokaaddons.webhookhandler.common.util.DateUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebHookApi {
    private @Autowired CommandInvoker invoker;
    private @Autowired DeadLetterStorage deadLetterStorage;
    private static final String SOURCE_NAME = WebHookApi.class.getName();
    private static final Logger LOGGER = LogManager.getFormatterLogger(WebHookApi.class);

    // it respresents the api endpoint which will be called by partner.
    public void handle(String message) throws Exception{
        LOGGER.debug("receice webhook message, with payload:%s", message);
        try{
            invoker.invoke(message);
        }
        catch(Exception ex){
            JsonNode msgNode = null;
            if(ex instanceof JsonCommandInvokerException )
                msgNode = ((JsonCommandInvokerException)ex).eventNode;

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