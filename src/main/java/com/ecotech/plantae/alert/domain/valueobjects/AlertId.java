package com.ecotech.plantae.alert.domain.valueobjects;

import java.util.Objects;
import java.util.UUID;

public record AlertId(String value) {

    public AlertId {
        Objects.requireNonNull(value, "Alert id cannot be null");
    }

    public static AlertId newId() {
        return new AlertId(UUID.randomUUID().toString());
    }

    public static AlertId of(String value) {
        return new AlertId(value);
    }
}
