package com.mekari.mokaaddons.common.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractVoidRequestHandler<TRequest extends Request<Void>>
        implements RequestHandler<TRequest, Void> {
    protected final Logger logger;

    protected AbstractVoidRequestHandler() {
        this.logger = LogManager.getFormatterLogger(this.getClass());
    }

    @Override
    public final Void handle(TRequest request) throws Exception {
        handleInternal(request);
        return null;
    }

    protected abstract void handleInternal(TRequest request) throws Exception;

}
