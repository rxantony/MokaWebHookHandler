package com.mekari.mokaaddons.common.handler;

public abstract class AbstractVoidRequestHandler<TRequest extends VoidRequest> implements RequestHandler<TRequest, Void> {

    @Override
    public final Void handle(TRequest request) {
        handleInternal(request);
        return null;
    }

    protected abstract void handleInternal(TRequest request);
    
}
