package com.mekari.mokaaddons.webhookhandler.common.storage;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public interface LockTrackerStorage {
    void insert(Item item) throws Exception;

    void delete(int connId, OffsetDateTime date) throws Exception;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private int connId;
        private String eventId;
        private String dataId;
        private String query;
        private OffsetDateTime createdAt;
    }
}
