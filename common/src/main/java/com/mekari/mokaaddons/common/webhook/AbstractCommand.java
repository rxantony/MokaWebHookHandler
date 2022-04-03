package com.mekari.mokaaddons.common.webhook;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

public abstract class AbstractCommand<TEvent extends Event> implements Command<TEvent> {

    private final Class<TEvent> eventCls;
    protected final Logger logger;

    protected AbstractCommand(Class<TEvent> eventCls) {
        Assert.notNull(eventCls, "eventCls must not be null");
        this.eventCls = eventCls;
        logger = LogManager.getFormatterLogger(this.getClass());
    }

    @Override
    public final Class<TEvent> eventClass() {
        return eventCls;
    }

    @Override
    public final void execute(TEvent event) throws CommandException{
        Assert.notNull(event, "event must not be null");
        Assert.notNull(event.getBody(), "event.body must not be null");

        try{
            executeInternal(event);
        }
        catch(Exception ex){
            if( ex instanceof CommandException)throw (CommandException)ex;
            throw new CommandException(ex, event);
        }
    }

    abstract protected void executeInternal(TEvent event) throws Exception;
}
