package com.mekari.mokaaddons.webhookhandler.common.event.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.mekari.mokaaddons.webhookhandler.common.command.UnknownEventFormatException;

public interface JsonEventValidator {
    void validate(JsonNode eventNode) throws UnknownEventFormatException;
}