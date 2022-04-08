package com.mekari.mokaaddons.common.handler;

import java.util.concurrent.CompletableFuture;

public interface AsyncVoidRequestHandler<TRequest extends Request<CompletableFuture<Void>>>
    extends RequestHandler<TRequest, CompletableFuture<Void>> {
}
