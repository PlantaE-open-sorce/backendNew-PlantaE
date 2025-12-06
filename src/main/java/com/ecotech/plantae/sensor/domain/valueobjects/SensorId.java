package com.ecotech.plantae.sensor.domain.valueobjects;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;
import java.util.UUID;

public record SensorId(String value) {

    public SensorId {
        Objects.requireNonNull(value, "Sensor id cannot be null");
    }

    public static SensorId newId() {
        return new SensorId(UUID.randomUUID().toString());
    }

    public static SensorId of(String value) {
        return value == null ? null : new SensorId(value);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
