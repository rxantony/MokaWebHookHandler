package com.mekari.mokaaddons.api.service.product.query.getall;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetAllProductResult {
    @Singular private List<Product> products;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class Product {
        private int id;
    }
}
