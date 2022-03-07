package com.mekari.mokaaddons.webhookhandler.common.event.validator;

public interface JsonEventValidatorManager {
    JsonEventValidator getDeafultValidator();
    JsonEventValidator crateValidator(String eventName);
}
