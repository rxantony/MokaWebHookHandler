
package com.mekari.mokaaddons.common.webhook.moka;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mekari.mokaaddons.common.webhook.EventHeader;

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
