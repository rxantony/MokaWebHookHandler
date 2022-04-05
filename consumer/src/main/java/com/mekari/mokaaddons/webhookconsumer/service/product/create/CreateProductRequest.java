package com.mekari.mokaaddons.webhookconsumer.service.product.create;

import java.time.OffsetDateTime;

import com.mekari.mokaaddons.common.handler.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CreateProductRequest implements Request<Boolean> {
    private String id;
    private String name;
    private String description;
    private OffsetDateTime createdAt;
}
