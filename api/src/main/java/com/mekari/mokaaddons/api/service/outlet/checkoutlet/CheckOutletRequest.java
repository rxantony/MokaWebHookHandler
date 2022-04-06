package com.mekari.mokaaddons.api.service.outlet.checkoutlet;

import com.mekari.mokaaddons.common.handler.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CheckOutletRequest implements Request<Boolean>{
    private int outletId;
}
