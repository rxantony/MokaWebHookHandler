package com.mekari.mokaaddons.api.service.outlet;

import java.util.List;

import com.mekari.mokaaddons.api.service.outlet.outletlist.OutletResult;

public interface OutletService {
    boolean checkActive(int outletId);
    List<OutletResult> getOutletList(int companyId);
}
