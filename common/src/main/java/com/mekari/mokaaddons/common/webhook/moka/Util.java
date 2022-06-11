
package com.mekari.mokaaddons.common.webhook.moka;

import com.mekari.mokaaddons.common.webhook.EventHandlingException;

public class Util {
    private Util(){
    }

    public static EventHandlingException eventNotFoundInEventSource(AbstractEvent event){
        return eventNotFoundInEventSource(event, null);
    }

    public static EventHandlingException eventNotFoundInEventSource(AbstractEvent event, String source){
        return new EventHandlingException(String.format("eventId:%s-bodyId:%s is not found on event_source", event.getHeader().getEventId(), event.getBody().getId()), event, source);
    }
}
