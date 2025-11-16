package com.ecotech.plantae.iam.domain.dtos;

public record AuthResponse(String token, String message, String accountType) {
}
