package com.mekari.mokaaddons.webhookconsumer.webhook.service.event.comparedate;

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
public class CompareEventDateRequest implements Request<CompareEventDateResult> {
    private String dataId;
    private OffsetDateTime evenDate;
}
