package com.mekari.mokaaddons.api.service.transaction.query.getall;

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
public class GetAllTransactionResult {
    @Singular private List<Transaction> transactions;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class Transaction {
        private int id;
    }
}
