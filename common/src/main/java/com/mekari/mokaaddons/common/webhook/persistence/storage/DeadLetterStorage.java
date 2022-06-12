package com.mekari.mokaaddons.common.webhook.persistence.storage;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public interface DeadLetterStorage{
    void insert(NewDeadLetter item) throws Exception;

    @Getter
    @Setter
    @Builder(builderClassName = "Builder")
    @AllArgsConstructor
    public class NewDeadLetter {
        private String source;
        private String eventId;
        private String payload;
        private String properties;
        private String reason;
        private OffsetDateTime createdAt;
    }
}
