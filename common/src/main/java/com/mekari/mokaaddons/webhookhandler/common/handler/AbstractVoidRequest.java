package com.mekari.mokaaddons.webhookhandler.common.handler;

public abstract class AbstractVoidRequest implements Request<Void> {
    public Class<Void> getResultClass(){
        return Void.class;
    }
}
