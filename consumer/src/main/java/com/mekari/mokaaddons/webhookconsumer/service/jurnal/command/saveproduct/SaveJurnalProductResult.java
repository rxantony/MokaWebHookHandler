package com.mekari.mokaaddons.webhookconsumer.service.jurnal.command.saveproduct;

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
public class SaveJurnalProductResult {

    @Singular private List<JurnalProduct> products;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class JurnalProduct {
        private String id;
        private String name;
    }
}
