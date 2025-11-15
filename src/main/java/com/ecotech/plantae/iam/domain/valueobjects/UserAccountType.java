package com.ecotech.plantae.iam.domain.valueobjects;

public enum UserAccountType {
    HOME,
    VIVERO_FORESTAL;

    public static UserAccountType from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Account type is required");
        }
        try {
            return UserAccountType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unsupported account type: " + value);
        }
    }
}
