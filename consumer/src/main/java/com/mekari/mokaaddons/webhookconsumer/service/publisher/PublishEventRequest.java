package com.mekari.mokaaddons.webhookconsumer.service.publisher;

import java.util.HashMap;
import java.util.Map;

import com.mekari.mokaaddons.common.handler.VoidRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PublishEventRequest implements VoidRequest {
    private String topic;
    private Object message;
    private final Map<String, String> properties = new HashMap<>();
}
