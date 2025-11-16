package com.ecotech.plantae.home.domain.dto;

public record HomeManualActionDto(
        String id,
        String actionType,
        String notes,
        String performedAt,
        int durationMinutes
) {
}
