package com.mekari.mokaaddons.api.service.product.command.posttojurnal;

import java.util.List;

import com.mekari.mokaaddons.common.handler.Request;
import com.mekari.mokaaddons.common.handler.RequestValidator;

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
public class PostProductToJurnalRequest implements Request<List<PostProductToJurnalResult>>, RequestValidator{
    @Singular private final List<JurnalProduct> products;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class JurnalProduct{
        private String id;
        private String name;
    }

    @Override
    public void validate() throws Exception {
        if(products == null) throw new IllegalArgumentException("products must not be null");
    }
}
