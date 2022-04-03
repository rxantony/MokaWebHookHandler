package com.mekari.mokaaddons.webhookconsumer.entity;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Item {
    private String id;
    private String jurnalId;
    private String name;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
