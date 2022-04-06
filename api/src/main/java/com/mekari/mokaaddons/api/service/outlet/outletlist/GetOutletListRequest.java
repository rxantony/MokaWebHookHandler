package com.mekari.mokaaddons.api.service.outlet.outletlist;

import java.util.List;

import com.mekari.mokaaddons.common.handler.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetOutletListRequest implements Request<List<OutletResult>>{
    private int companyId;
}
