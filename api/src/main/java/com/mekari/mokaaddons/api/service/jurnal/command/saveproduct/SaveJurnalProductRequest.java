package com.mekari.mokaaddons.api.service.jurnal.command.saveproduct;

import java.util.List;

import com.mekari.mokaaddons.common.handler.Request;
import com.mekari.mokaaddons.common.handler.Validateable;

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
public class SaveJurnalProductRequest implements Request<List<SaveJurnalProductResult>>, Validateable{
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
