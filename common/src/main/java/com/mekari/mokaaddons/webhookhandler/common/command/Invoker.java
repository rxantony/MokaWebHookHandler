package com.mekari.mokaaddons.webhookhandler.common.command;

public interface Invoker {
    void invoke(String message) throws Exception;
}
