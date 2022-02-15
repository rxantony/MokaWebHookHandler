package com.mekari.mokaaddons.webhookhandler.common.util;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class DateUtil {
    public static OffsetDateTime now(){
        return Instant.now().atOffset(ZoneOffset.UTC);
    }
}
