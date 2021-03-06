package com.mekari.mokaaddons.api.service.product.command.save;

import java.util.List;

import com.mekari.mokaaddons.common.handler.Validateable;
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
public class SaveProductRequest implements VoidRequest, Validateable{
    @Singular private final List<Product> products;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class Product{
        private String mokaItemId;
        private String name;
    }

    @Override
    public void validate() throws Exception {
        if(products.isEmpty()) throw new IllegalArgumentException("products atleast must contains one item");
    }
}
