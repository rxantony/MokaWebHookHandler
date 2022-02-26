package com.mekari.mokaaddons.webhookhandler.common.command;

public interface JsonEventValidatorManager {
    JsonEventValidator getDeafultValidator();
    JsonEventValidator crateValidator(String eventName);
}
