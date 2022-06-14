
package com.mekari.mokaaddons.common.webhook.event.moka;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mekari.mokaaddons.common.webhook.event.EventHeader;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MokaEventHeader extends EventHeader  {

    public MokaEventHeader(MokaEventHeader from){
        super(from);

        outletId = from.outletId;
        version = from.version;
    }

    @JsonProperty("outlet_id")
    private String outletId;

    @JsonProperty("version")
    private int version;
}
