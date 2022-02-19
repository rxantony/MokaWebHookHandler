package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

public abstract class AbstractCommandEvent<TEvent extends Event> implements CommandEvent<TEvent> {

    private final Class<TEvent> eventCls;
    protected final Logger logger;

    protected AbstractCommandEvent(Class<TEvent> eventCls) {
        Assert.notNull(eventCls, "eventCls must not be null");
        this.eventCls = eventCls;
        logger = LogManager.getFormatterLogger(this.getClass());
    }

    @Override
    public final Class<TEvent> eventClass() {
        return eventCls;
    }

    @Override
    public final void execute(TEvent event) throws CommandEventException{

        try{
            executeInternal(event);
        }
        catch(Exception ex){
            if( ex instanceof CommandEventException)throw (CommandEventException)ex;
            throw new CommandEventException(ex, event);
        }
    }

    abstract protected void executeInternal(TEvent event) throws Exception;
}
