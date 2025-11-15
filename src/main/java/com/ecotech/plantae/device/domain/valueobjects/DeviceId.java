package com.ecotech.plantae.device.domain.valueobjects;

import java.util.Objects;

public record DeviceId(String value) {

    public DeviceId {
        Objects.requireNonNull(value, "Device id cannot be null");
    }

    public static DeviceId of(String value) {
        return new DeviceId(value);
    }
}
