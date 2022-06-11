package com.mekari.mokaaddons.webhookconsumer.service.event.command.comparedate;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CompareEventDateResult {
    private String dataId;
    private OffsetDateTime eventDate;
    private OffsetDateTime lastEventDate;
    private boolean isEqualsWithLastEventDate;
}
