package com.mekari.mokaaddons.common.handler;

import java.util.concurrent.CompletableFuture;

public interface AsyncRequestHandler<TRequest extends Request<CompletableFuture<TResult>>, TResult>
    extends RequestHandler<TRequest, CompletableFuture<TResult>>{
}
