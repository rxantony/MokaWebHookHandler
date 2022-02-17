package com.mekari.mokaaddons.webhookhandler;

import com.mekari.mokaaddons.webhookhandler.common.command.CommandInvoker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebHookApi {
    private @Autowired CommandInvoker invoker;
    private static final Logger LOGGER = LogManager.getFormatterLogger(WebHookApi.class);

    // it respresents the api endpoint which will be called by partner.
    public void handle(String message) throws Exception{
        LOGGER.debug("receice webhook message, with payload:%s", message);
        invoker.invoke(message);
    }
}