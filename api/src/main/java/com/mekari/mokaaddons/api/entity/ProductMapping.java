package com.mekari.mokaaddons.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductMapping {
    private int id;
    private String mokaId;
    private String jurnalId;
    private String name;
}
