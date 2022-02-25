package com.mekari.mokaaddons.webhookhandler.common.storage;

import java.time.OffsetDateTime;
import java.util.Optional;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public interface EventSourceStorage {

    Optional<OffsetDateTime> getEventDate(Event event) throws Exception;
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
