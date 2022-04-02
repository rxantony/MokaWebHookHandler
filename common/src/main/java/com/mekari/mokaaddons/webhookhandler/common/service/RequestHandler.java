package com.mekari.mokaaddons.webhookhandler.common.service;

public interface RequestHandler<TRequest extends Request<TResult>, TResult> extends RawRequestHandler<TRequest> {
    TResult handle(TRequest request);
}
