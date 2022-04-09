package com.mekari.mokaaddons.api.service.outlet.checkoutlet;

import com.mekari.mokaaddons.common.handler.RequestHandler;

import org.springframework.stereotype.Service;

/**
 * sample request handler
 */
@Service
public class CheckOutletRequestHandler implements RequestHandler<CheckOutletRequest, Boolean> {

    @Override
    public Boolean handle(CheckOutletRequest request) throws Exception {
        if(request.getOutletId() < 3) return false;
        return true;
    }
    
}
