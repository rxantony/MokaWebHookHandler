package com.mekari.mokaaddons.api.service.product.createtojurnal;

import java.util.List;

import com.mekari.mokaaddons.api.model.JurnalProduct;
import com.mekari.mokaaddons.common.handler.Request;

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
public class CreateProductToJurnalRequest implements Request<List<JurnalProduct>>{
    @Singular private final List<NewJurnalProduct> products;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class NewJurnalProduct{
        private String name;
    }
}
