package com.mekari.mokaaddons.api.service.webhook.handleEvent;

import com.mekari.mokaaddons.common.handler.VoidRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class HandleEventRequest implements VoidRequest{
    private String json;
    private String sourceName;
}
