package com.mekari.mokaaddons.webhookhandler.service.product.query.productExists;

import com.mekari.mokaaddons.webhookhandler.common.handler.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductExistsRequest implements Request<Boolean> {
    private String id;
}
