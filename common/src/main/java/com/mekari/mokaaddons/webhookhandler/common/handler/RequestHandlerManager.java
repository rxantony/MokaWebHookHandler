package com.mekari.mokaaddons.webhookhandler.common.handler;

public interface RequestHandlerManager {
    <TRequest extends RequestParam<TResult>, TResult> TResult handle(TRequest request) throws Exception;
}

