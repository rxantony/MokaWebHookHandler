package com.mekari.mokaaddons.webhookhandler.common.storage;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public interface DeadLetterStorage{
    void insert(NewItem item) throws Exception;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public class NewItem {
        private String source;
        private String eventId;
        private String payload;
        private String properties;
        private String reason;
        private OffsetDateTime createdAt;
    }
}
