package com.mekari.mokaaddons.webhookhandler.common.service;

public interface RequestHandlerManager {
    <TRequest extends Request<TResult>, TResult> TResult handle(TRequest request) throws Exception;
}

