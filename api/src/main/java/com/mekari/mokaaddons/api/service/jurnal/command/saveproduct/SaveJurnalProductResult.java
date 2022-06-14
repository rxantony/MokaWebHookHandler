package com.mekari.mokaaddons.api.service.jurnal.command.saveproduct;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SaveJurnalProductResult {
    private String id;
    private String name;
}
