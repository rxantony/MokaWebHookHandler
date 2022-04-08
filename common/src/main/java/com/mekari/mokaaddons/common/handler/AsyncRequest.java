package com.mekari.mokaaddons.common.handler;

import java.util.concurrent.CompletableFuture;

public interface AsyncRequest<TResult> extends Request<CompletableFuture<TResult>> {
    
}
