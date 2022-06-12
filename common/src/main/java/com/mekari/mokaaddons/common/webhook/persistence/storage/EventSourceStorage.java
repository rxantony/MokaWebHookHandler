package com.mekari.mokaaddons.common.webhook.persistence.storage;

import java.time.OffsetDateTime;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public interface EventSourceStorage {

    Optional<OffsetDateTime> getLastEventDate(String dataId) throws Exception;

    void insert(NewEventSource eventSource) throws Exception;

    @Getter
    @Setter
    @Builder(builderClassName = "Builder")
    @AllArgsConstructor
    public class NewEventSource {
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
