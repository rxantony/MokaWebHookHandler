package com.mekari.mokaaddons.common.handler;

public interface RawRequestHandler<TRequest> {
    Object handle(TRequest request) throws Exception;
}
