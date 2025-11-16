package com.ecotech.plantae.profile.domain.dtos;

import jakarta.validation.constraints.NotBlank;

public record NotificationPreferenceDto(@NotBlank String type, boolean emailEnabled, boolean inAppEnabled) {}
