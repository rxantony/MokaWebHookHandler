package com.mekari.mokaaddons.webhookconsumer.service.product.exists;

import com.mekari.mokaaddons.common.handler.Request;

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
