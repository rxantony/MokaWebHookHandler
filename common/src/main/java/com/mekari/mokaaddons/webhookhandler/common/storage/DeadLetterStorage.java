package com.mekari.mokaaddons.webhookhandler.common.storage;

import java.math.BigInteger;
import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public interface DeadLetterStorage{
    void insert(DeadLeterItem item) throws Exception;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public class DeadLeterItem {
        private BigInteger id;
        private String source;
        private String eventId;
        private String payload;
        private String properties;
        private OffsetDateTime createdAt;
    }
}
