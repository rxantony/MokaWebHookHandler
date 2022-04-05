package com.mekari.mokaaddons.common.webhook;

import java.time.OffsetDateTime;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public interface EventSourceStorage {

    Optional<OffsetDateTime> getLastEventDate(String dataId) throws Exception;

    void insert(NewItem item) throws Exception;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public class NewItem {
        private String dataId;
        private OffsetDateTime eventDate;
        private String eventName;
        private String payload;
        private String eventId;
        private String outletId;
        private int version;
        private OffsetDateTime timestamp;
        private OffsetDateTime createdAt;
    }
}
