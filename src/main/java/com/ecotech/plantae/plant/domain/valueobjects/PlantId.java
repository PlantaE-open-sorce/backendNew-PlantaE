package com.ecotech.plantae.plant.domain.valueobjects;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;
import java.util.UUID;

public record PlantId(String value) {

    public PlantId {
        Objects.requireNonNull(value, "Plant id cannot be null");
    }

    public static PlantId newId() {
        return new PlantId(UUID.randomUUID().toString());
    }

    public static PlantId of(String value) {
        return new PlantId(value);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
