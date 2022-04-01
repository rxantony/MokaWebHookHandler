package com.mekari.mokaaddons.webhookhandler.common.handler;

public interface RawRequestHandler<TRequest> {
    Object handle(TRequest request);
}
