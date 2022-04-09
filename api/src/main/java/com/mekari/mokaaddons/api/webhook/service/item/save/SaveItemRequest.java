package com.mekari.mokaaddons.api.webhook.service.item.save;

import com.mekari.mokaaddons.common.handler.VoidRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SaveItemRequest implements VoidRequest {
    private String json;
}
