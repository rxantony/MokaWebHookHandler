package com.mekari.mokaaddons.common.handler;

public interface RequestHandler<TRequest extends Request<TResult>, TResult> extends RawRequestHandler<TRequest> {
    TResult handle(TRequest request) throws Exception;
}
