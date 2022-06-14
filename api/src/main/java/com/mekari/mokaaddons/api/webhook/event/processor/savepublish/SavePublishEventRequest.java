package com.mekari.mokaaddons.api.webhook.event.processor.savepublish;

import com.mekari.mokaaddons.common.handler.VoidRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SavePublishEventRequest implements VoidRequest{
    private String json;
    private String sourceName;
}
