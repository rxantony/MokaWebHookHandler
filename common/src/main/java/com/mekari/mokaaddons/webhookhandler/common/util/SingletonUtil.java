package com.mekari.mokaaddons.webhookhandler.common.util;

import com.mekari.mokaaddons.webhookhandler.common.event.DefaultJsonEventValidator;

public class SingletonUtil {
    public static final DefaultJsonEventValidator DEFAULT_JSONEVENT_VALIDATOR = new DefaultJsonEventValidator();
}
