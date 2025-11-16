package com.ecotech.plantae.iam.domain.valueobjects;

public enum UserLanguage {
    EN,
    ES;

    public static UserLanguage from(String value) {
        if (value == null || value.isBlank()) {
            return EN;
        }
        try {
            return UserLanguage.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return EN;
        }
    }
}
