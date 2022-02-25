package com.mekari.mokaaddons.webhookhandler.common.event.moka;

import java.time.OffsetDateTime;

import com.mekari.mokaaddons.webhookhandler.common.event.EventData;

public interface MokaEventData extends EventData{
    public OffsetDateTime getDate();
}
