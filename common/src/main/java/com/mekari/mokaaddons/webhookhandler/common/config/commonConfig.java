package com.mekari.mokaaddons.webhookhandler.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.webhookhandler.common.command.CommandEventManager;
import com.mekari.mokaaddons.webhookhandler.common.command.DefaultInvoker;
import com.mekari.mokaaddons.webhookhandler.common.command.Invoker;
import com.mekari.mokaaddons.webhookhandler.common.command.SpringbootCommandEventManager;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class commonConfig {

    @Bean()
    public CommandEventManager springbootCommandManager(ApplicationContext ctx){
        return new SpringbootCommandEventManager(ctx);
    }

    @Bean()
    public Invoker defaultInvoker(CommandEventManager manager, ObjectMapper mapper){
        return new DefaultInvoker(manager, mapper);
    }
    
}
