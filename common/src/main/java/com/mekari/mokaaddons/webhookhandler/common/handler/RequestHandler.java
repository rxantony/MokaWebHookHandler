package com.mekari.mokaaddons.webhookhandler.common.handler;

public interface RequestHandler<TRequest extends Request<TResult>, TResult> {
    TResult handle(TRequest request);
}
