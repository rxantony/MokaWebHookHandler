package com.mekari.mokaaddons.webhookhandler.common.event;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventData {
    private String id;

    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;
}
