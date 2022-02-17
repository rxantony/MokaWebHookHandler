package com.mekari.mokaaddons.webhookhandler.common.event;

import com.fasterxml.jackson.databind.JsonNode;

public interface JsonEventValidator {
    void validate(JsonNode eventNode) throws UnknownEventFormatException;
}
