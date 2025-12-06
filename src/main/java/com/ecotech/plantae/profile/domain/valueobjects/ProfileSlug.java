package com.ecotech.plantae.profile.domain.valueobjects;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

public record ProfileSlug(@JsonValue String value) {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 50;

    public ProfileSlug {
        Objects.requireNonNull(value, "Profile slug cannot be null");
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Slug must be between %d and %d characters".formatted(MIN_LENGTH, MAX_LENGTH));
        }
    }

    public static ProfileSlug of(String input) {
        String normalized = normalize(input);
        return new ProfileSlug(normalized);
    }

    private static String normalize(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Slug cannot be null");
        }
        String normalized = input.trim().toLowerCase();
        normalized = normalized.replaceAll("\\s+", "-");
        normalized = normalized.replaceAll("[^a-z0-9-]", "");
        normalized = normalized.replaceAll("-+", "-");
        normalized = normalized.replaceAll("^-|-$", "");
        if (normalized.length() < MIN_LENGTH || normalized.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Slug must be between %d and %d characters".formatted(MIN_LENGTH, MAX_LENGTH));
        }
        return normalized;
    }
}
