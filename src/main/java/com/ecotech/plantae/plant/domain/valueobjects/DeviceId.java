package com.ecotech.plantae.plant.domain.valueobjects;

import java.util.Objects;

public record DeviceId(String value) {

    public DeviceId {
        Objects.requireNonNull(value, "Device id cannot be null");
        String normalized = value.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Device id cannot be blank");
        }
        value = normalized;
    }

    public static DeviceId of(String value) {
        if (value == null) {
            return null;
        }
        return new DeviceId(value);
    }
}
