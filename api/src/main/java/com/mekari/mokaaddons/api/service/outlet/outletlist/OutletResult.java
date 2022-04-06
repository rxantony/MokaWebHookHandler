package com.mekari.mokaaddons.api.service.outlet.outletlist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OutletResult {
    private int id;
    private boolean isActive;
}
