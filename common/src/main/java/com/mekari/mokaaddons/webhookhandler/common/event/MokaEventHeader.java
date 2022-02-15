package com.mekari.mokaaddons.webhookhandler.common.event;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MokaEventHeader extends EventHeader  {

    @JsonProperty("outlet_id")
    private String outletId;

    @JsonProperty("version")
    private int version;

    @JsonProperty("timestamp")
    private OffsetDateTime timestamp;
}
