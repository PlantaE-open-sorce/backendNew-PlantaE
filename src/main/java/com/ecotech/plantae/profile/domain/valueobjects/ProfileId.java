package com.ecotech.plantae.profile.domain.valueobjects;

import java.util.Objects;
import java.util.UUID;

public record ProfileId(String value) {

    public ProfileId {
        Objects.requireNonNull(value, "Profile id cannot be null");
    }

    public static ProfileId newId() {
        return new ProfileId(UUID.randomUUID().toString());
    }

    public static ProfileId of(String value) {
        return new ProfileId(value);
    }
}
