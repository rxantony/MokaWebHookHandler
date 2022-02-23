package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

public abstract class AbstractEventCommand<TEvent extends Event> implements EventCommand<TEvent> {

    private final Class<TEvent> eventCls;
    protected final Logger logger;

    protected AbstractEventCommand(Class<TEvent> eventCls) {
        Assert.notNull(eventCls, "eventCls must not be null");
        this.eventCls = eventCls;
        logger = LogManager.getFormatterLogger(this.getClass());
    }

    @Override
    public final Class<TEvent> eventClass() {
        return eventCls;
    }

    @Override
    public final void execute(TEvent event) throws EventCommandException{
        Assert.notNull(event, "event must not be null");
        Assert.notNull(event.getBody(), "event.body must not be null");

        try{
            executeInternal(event);
        }
        catch(Exception ex){
            if( ex instanceof EventCommandException)throw (EventCommandException)ex;
            throw new EventCommandException(ex, event);
        }
    }

    abstract protected void executeInternal(TEvent event) throws Exception;
}
