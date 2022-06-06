package com.mekari.mokaaddons.webhookconsumer.service.product.command.posttojurnal;

import java.util.List;

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
public class PostProductToJurnalRequest implements Request<List<PostProductToJurnalResult>>{
    @Singular private final List<JurnalProduct> products;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class JurnalProduct{
        private String id;
        private String name;
    }
}
