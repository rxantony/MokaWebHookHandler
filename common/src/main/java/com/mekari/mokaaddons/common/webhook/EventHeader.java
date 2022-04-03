package com.mekari.mokaaddons.common.webhook;

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
public class EventHeader implements Cloneable {

    @JsonProperty("event_id")
    private String eventId;

    @With
    @JsonProperty("event_name")
    private String eventName;

    @JsonProperty("timestamp")
    private OffsetDateTime timestamp;

    public Object clone()throws CloneNotSupportedException{  
        return super.clone();  
    }  
}
