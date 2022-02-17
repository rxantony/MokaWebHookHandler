package com.mekari.mokaaddons.webhookhandler;

import com.mekari.mokaaddons.webhookhandler.common.command.DefaultCommandEventInvoker;
import com.mekari.mokaaddons.webhookhandler.common.event.Event;
import com.mekari.mokaaddons.webhookhandler.common.storage.DeadLetterStorage;
import com.mekari.mokaaddons.webhookhandler.common.storage.DeadLetterStorage.Item;
import com.mekari.mokaaddons.webhookhandler.common.util.DateUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class WebHookCommandInvoker extends DefaultCommandEventInvoker {
    private @Autowired DeadLetterStorage deadLetterStorage;

    private static final String SOURCE_NAME = WebHookApi.class.getName();

    @Override
    protected void afterInvoked(String message, Event event, Exception ex) throws Exception {
        if(ex == null)
            return;

        try{
            var builder = Item.builder()
                            .payload(message)
                            .reason(ex.toString())
                            .source(SOURCE_NAME)
                            .createdAt(DateUtil.now());
            deadLetterStorage.insert(builder.build());
        }
        catch(Exception iex){
            getLogger().error(iex.toString());
        }
    }
}
