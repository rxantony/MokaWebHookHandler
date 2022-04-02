package com.mekari.mokaaddons.webhookhandler.service.product.command.addProduct;

import java.time.OffsetDateTime;

import com.mekari.mokaaddons.webhookhandler.common.service.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AddProductRequest implements Request<Boolean> {
    private String id;
    private String name;
    private String description;
    private OffsetDateTime createdAt;
}
