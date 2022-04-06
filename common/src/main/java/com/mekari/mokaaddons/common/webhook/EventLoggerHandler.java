package com.mekari.mokaaddons.common.webhook;

import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.handler.VoidRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventLoggerHandler<TRequest extends VoidRequest> extends AbstractVoidRequestHandler<TRequest> {

    private AbstractVoidRequestHandler<TRequest> next;
    private final static Logger logger = LogManager.getFormatterLogger(EventLoggerHandler.class);

    public EventLoggerHandler(AbstractVoidRequestHandler<TRequest> next) {
        this.next = next;
    }

    @Override
    protected void handleInternal(TRequest request) throws Exception {
        logger.debug("start executing");
        next.handle(request);
        logger.debug("end executing");
    }
    
}
