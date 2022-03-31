package com.mekari.mokaaddons.webhookhandler.common.handler;

public interface RequestHandler<TRequest extends RequestParam<TResult>, TResult> {
    TResult handle(TRequest request);
}
