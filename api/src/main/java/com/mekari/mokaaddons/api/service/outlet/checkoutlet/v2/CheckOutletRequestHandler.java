package com.mekari.mokaaddons.api.service.outlet.checkoutlet.v2;

import com.mekari.mokaaddons.api.service.outlet.checkoutlet.CheckOutletRequest;
import com.mekari.mokaaddons.common.handler.RequestHandler;

/**
 * sample request handler
 */
public class CheckOutletRequestHandler implements RequestHandler<CheckOutletRequest, Boolean> {

    @Override
    public Boolean handle(CheckOutletRequest request) throws Exception {
        if(request.getOutletId() < 3) return true;
        return false;
    }
    
}