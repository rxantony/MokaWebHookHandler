package com.mekari.mokaaddons.webhookhandler.common.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventHeader {

    @JsonProperty("event_id")
    private String eventId;

    @JsonProperty("event_name")
    private String eventName;
}
