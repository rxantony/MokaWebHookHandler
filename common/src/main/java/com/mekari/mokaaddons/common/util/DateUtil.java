package com.mekari.mokaaddons.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

public class DateUtil {
    private DateUtil() {}
    
    public static OffsetDateTime utcNow(){
        return Instant.now().atOffset(ZoneOffset.UTC);
    }

    public static LocalDate convertToLocalDate(Date fromDate) {
        return convertToLocalDate(fromDate, ZoneId.systemDefault());
    }

    public static LocalDate convertToLocalDate(Date fromDate, ZoneId zone) {
        return fromDate.toInstant()
          .atZone(zone)
          .toLocalDate();
    }

    public static Date convertToDate(LocalDate fromDate) {
        return convertToDate(fromDate, ZoneId.systemDefault());
    }

    public static Date convertToDate(LocalDate fromDate, ZoneId zone) {
        return java.util.Date.from(fromDate.atStartOfDay()
          .atZone(zone)
          .toInstant());
    }
}
