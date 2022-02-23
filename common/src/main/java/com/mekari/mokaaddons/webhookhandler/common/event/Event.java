package com.mekari.mokaaddons.webhookhandler.common.event;

import java.time.OffsetDateTime;

public interface Event {
    String geId();
    String getName();
    OffsetDateTime getDate();
    EventBody getBody();
}