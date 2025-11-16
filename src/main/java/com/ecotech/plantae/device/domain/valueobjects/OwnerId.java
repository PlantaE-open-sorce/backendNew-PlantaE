package com.ecotech.plantae.device.domain.valueobjects;

import java.util.Objects;

public record OwnerId(String value) {

    public OwnerId {
        Objects.requireNonNull(value, "Owner id cannot be null");
    }

    public static OwnerId of(String value) {
        return new OwnerId(value);
    }
}
