package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.event.JsonEventValidator;

public interface JsonEventValidatorManager {
    JsonEventValidator getDeafultValidator();
    JsonEventValidator crateValidator(String eventName);
}
