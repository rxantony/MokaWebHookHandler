package com.mekari.mokaaddons.webhookhandler;

import com.mekari.mokaaddons.webhookhandler.common.command.DefaultCommandEventInvoker;
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
    protected void afterInvoked(AfterInvokedContext context) throws Exception {
        if(context.exception == null)
            return;

        try{
            var builder = Item.builder()
                            .payload(context.event)
                            .reason(context.exception.toString())
                            .source(SOURCE_NAME)
                            .createdAt(DateUtil.now());
            if(context.eventNode != null){                
                var header = context.eventNode.get("header");
                if (header != null) {
                    var event_id = header.get("event_id");
                    if (event_id != null)
                        builder.eventId(event_id.asText());
                }
            }
            deadLetterStorage.insert(builder.build());
        }
        catch(Exception iex){
            getLogger().error(iex.toString());
        }
    }
}
