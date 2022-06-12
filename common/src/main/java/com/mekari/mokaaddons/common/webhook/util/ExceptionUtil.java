
package com.mekari.mokaaddons.common.webhook.util;

import com.mekari.mokaaddons.common.webhook.EventHandlingException;
import com.mekari.mokaaddons.common.webhook.moka.AbstractEvent;

public class ExceptionUtil {
    private ExceptionUtil(){
    }

    public static EventHandlingException eventNotFoundInEventSource(AbstractEvent event){
        return eventNotFoundInEventSource(event, null);
    }

    public static EventHandlingException eventNotFoundInEventSource(AbstractEvent event, String source){
        return new EventHandlingException(String.format("eventId:%s-bodyId:%s is not found on event_source", event.getHeader().getEventId(), event.getBody().getId()), event, source);
    }
}
