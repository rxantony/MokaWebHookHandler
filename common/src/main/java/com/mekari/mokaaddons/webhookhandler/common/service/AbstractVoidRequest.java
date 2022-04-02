package com.mekari.mokaaddons.webhookhandler.common.service;

public abstract class AbstractVoidRequest implements Request<Void> {
    public Class<Void> getResultClass(){
        return Void.class;
    }
}
