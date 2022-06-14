package com.mekari.mokaaddons.common.webhook.event;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventHeader {

    public EventHeader(EventHeader from){
        eventId = from.eventId;
        eventName = from.eventName;
        timestamp = from.timestamp;
    }

    @JsonProperty("event_id")
    private String eventId;

    @With
    @JsonProperty("event_name")
    private String eventName;

    @JsonProperty("timestamp")
    private OffsetDateTime timestamp;
}
