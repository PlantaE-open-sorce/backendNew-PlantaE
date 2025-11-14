package com.ecotech.plantae.plant.domain.dtos;

public record PlantDto(
        String id,
        String ownerId,
        String name,
        String species,
        String status,
        String deviceId,
        String sensorId,
        boolean hasAlerts,
        String createdAt,
        String updatedAt
) {
}
