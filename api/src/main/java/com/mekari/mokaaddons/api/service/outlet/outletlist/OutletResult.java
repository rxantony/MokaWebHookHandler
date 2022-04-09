package com.mekari.mokaaddons.api.service.outlet.outletlist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * sample request result
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OutletResult {
    private int id;
    private boolean isActive;
}
