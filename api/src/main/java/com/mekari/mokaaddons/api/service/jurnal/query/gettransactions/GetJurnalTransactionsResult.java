package com.mekari.mokaaddons.api.service.jurnal.query.gettransactions;

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
public class GetJurnalTransactionsResult {
    @Singular private List<JurnalTransaction> transactions;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class JurnalTransaction {
        private int id;
    }
}
