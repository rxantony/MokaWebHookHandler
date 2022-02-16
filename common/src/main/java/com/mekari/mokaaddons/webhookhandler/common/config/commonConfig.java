package com.mekari.mokaaddons.webhookhandler.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.webhookhandler.common.command.CommandManager;
import com.mekari.mokaaddons.webhookhandler.common.command.DefaultInvoker;
import com.mekari.mokaaddons.webhookhandler.common.command.Invoker;
import com.mekari.mokaaddons.webhookhandler.common.command.SpringbootCommandManager;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class commonConfig {

    @Bean()
    public CommandManager springbootCommandManager(ApplicationContext ctx){
        return new SpringbootCommandManager(ctx);
    }

    @Bean()
    public Invoker defaultInvoker(CommandManager manager, ObjectMapper mapper){
        return new DefaultInvoker(manager, mapper);
    }
    
}
