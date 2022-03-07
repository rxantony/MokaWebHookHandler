package com.mekari.mokaaddons.webhookhandler.common.util;

import com.mekari.mokaaddons.webhookhandler.common.event.validator.JsonEventValidatorDefault;

public class SingletonUtil {
    public static final JsonEventValidatorDefault DEFAULT_JSONEVENT_VALIDATOR = new JsonEventValidatorDefault();
}
