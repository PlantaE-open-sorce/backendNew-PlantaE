package com.ecotech.plantae.iam.application.ports;

public interface PasswordHasher {
    String hash(String raw);
    boolean matches(String raw, String hashed);
}
