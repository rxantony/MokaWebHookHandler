package com.mekari.mokaaddons.webhookhandler.common.event.moka;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mekari.mokaaddons.webhookhandler.common.event.EventHeader;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MokaEventHeader extends EventHeader  {
    @JsonProperty("outlet_id")
    private String outletId;

    @JsonProperty("version")
    private int version;
}
