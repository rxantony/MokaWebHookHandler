package com.mekari.mokaaddons.webhookhandler.common.storage;

import java.time.OffsetDateTime;
import java.util.Optional;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;

public interface EventSourceStorage {
    Optional<OffsetDateTime> getEventDate(Event event) throws Exception;
}
