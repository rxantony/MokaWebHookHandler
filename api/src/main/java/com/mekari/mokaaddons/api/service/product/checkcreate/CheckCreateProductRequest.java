package com.mekari.mokaaddons.api.service.product.checkcreate;

import java.util.List;

import com.mekari.mokaaddons.common.handler.VoidRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

/**
 * sample request
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CheckCreateProductRequest implements VoidRequest{
    private @Singular final List<Product> products;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class Product{
        private String mokaId;
        private String name;
    }
}
