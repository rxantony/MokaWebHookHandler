package com.mekari.mokaaddons.webhookhandler.service.product.command.updateProduct;

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
public class UpdateProductRequest implements Request<Boolean> {
    private String id;
    private String name;
    private String description;
    private OffsetDateTime updatedAt;
}
