package com.mekari.mokaaddons.common.webhook;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public interface LockTrackerStorage {
    
    void insert(NewItem item) throws Exception;

    void delete(int connId, OffsetDateTime date) throws Exception;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NewItem {
        private int connId;
        private int trxId;
        private String eventId;
        private String eventName;;
        private String dataId;
        private String query;
        private OffsetDateTime trxStarted;
        private OffsetDateTime createdAt;
    }
}
