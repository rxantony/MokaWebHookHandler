package com.mekari.mokaaddons.common.handler;

/**
 * needd by SpringbootRequestHandlerManager to aliviate its tasks and as well to make our code clean.
 */
interface RawRequestHandler<TRequest> {
    Object handle(TRequest request) throws Exception;
}
