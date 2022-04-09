package com.mekari.mokaaddons.api.service.outlet.outletlist;

import java.util.ArrayList;
import java.util.List;

import com.mekari.mokaaddons.api.service.outlet.checkoutlet.CheckOutletRequest;
import com.mekari.mokaaddons.common.handler.RequestHandler;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * sample request handler
 */
@Service
public class GetOutletListRequestHandler implements RequestHandler<GetOutletListRequest, List<OutletResult>> {

    private @Autowired RequestHandlerManager requestManager;

    @Override
    public List<OutletResult> handle(GetOutletListRequest request) throws Exception {
        var result = new ArrayList<OutletResult>();
        result.add(new OutletResult(1, false));
        result.add(new OutletResult(2, false));
        result.add(new OutletResult(3, false));
        result.add(new OutletResult(4, false));

       for(var r : result) {
            var checkRequest = CheckOutletRequest.builder()
                                    .outletId(r.getId())
                                    .build();

            var active = requestManager.handle(checkRequest);
            r.setActive(active);
       }

       return result;
    }
    
}
