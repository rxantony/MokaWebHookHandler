package com.mekari.mokaaddons.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class JurnalProduct {
    private String id;
    private String name;
}
