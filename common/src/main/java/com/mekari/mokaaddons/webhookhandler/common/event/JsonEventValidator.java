package com.mekari.mokaaddons.webhookhandler.common.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.mekari.mokaaddons.webhookhandler.common.WebHookHandlingException;

public interface JsonEventValidator {
    void validate(JsonNode eventNode) throws WebHookHandlingException;
}
