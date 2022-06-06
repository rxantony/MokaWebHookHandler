package com.mekari.mokaaddons.api.service.product.command.posttojurnal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostProductToJurnalResult {
    private String id;
    private String name;
}
