package com.ecotech.plantae.iam.application.internal.outboundservices;

public interface PasswordHasher {
    String hash(String raw);
    boolean matches(String raw, String hashed);
}
