package com.mekari.mokaaddons.webhookhandler.common.handler;

public abstract class AbstractVoidRequestHandler<TRequest extends AbstractVoidRequest> implements RequestHandler<TRequest, Void> {

    @Override
    public final Void handle(TRequest request) {
        handleInternal(request);
        return null;
    }

    protected abstract void handleInternal(TRequest request);
    
}
