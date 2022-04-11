package com.mekari.mokaaddons.api.service.product.syncmanual;

import java.util.Date;

import com.mekari.mokaaddons.common.handler.VoidRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SyncProductManualRequest implements VoidRequest{
    private Date from;
    private Date to;
}