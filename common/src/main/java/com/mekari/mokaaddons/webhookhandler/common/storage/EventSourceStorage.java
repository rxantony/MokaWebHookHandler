package com.mekari.mokaaddons.webhookhandler.common.storage;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface EventSourceStorage {
    Optional<OffsetDateTime> getUpdateAt(String id) throws Exception;
}
