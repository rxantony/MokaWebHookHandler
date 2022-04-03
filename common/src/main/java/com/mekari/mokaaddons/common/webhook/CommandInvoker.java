package com.mekari.mokaaddons.common.webhook;

public interface CommandInvoker {
    void invoke(String message) throws CommandInvokerException;
}
