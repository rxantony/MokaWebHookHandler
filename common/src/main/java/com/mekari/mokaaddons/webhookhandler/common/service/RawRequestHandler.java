package com.mekari.mokaaddons.webhookhandler.common.service;

public interface RawRequestHandler<TRequest> {
    Object handle(TRequest request);
}
