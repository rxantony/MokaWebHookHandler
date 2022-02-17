package com.mekari.mokaaddons.webhookhandler.common.command;

public interface CommandInvoker {
    void invoke(String message) throws Exception;
}
