package com.mekari.mokaaddons.api.service.product.command.sync;

import java.time.Period;
import java.util.Date;

import com.mekari.mokaaddons.common.handler.Validateable;
import com.mekari.mokaaddons.common.handler.VoidRequest;
import com.mekari.mokaaddons.common.util.DateUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SyncProductRequest implements VoidRequest, Validateable{
    private Date from;
    private Date to;

    @Override
    public void validate() throws Exception {
        if(from == null) throw new IllegalArgumentException("from must not be null");
        if(to == null) throw new IllegalArgumentException("to must not be null");
        if(from.after(to)) throw new IllegalArgumentException("from must be lower than or equals to to");
        
        var fldate = DateUtil.convertToLocalDate(from);
        var tldate = DateUtil.convertToLocalDate(to);
        var period = Period.between(fldate, tldate);
        if(Math.abs(period.getDays()) > 31) throw new IllegalArgumentException("fro to period must not greater than 31 days");
    }
}
