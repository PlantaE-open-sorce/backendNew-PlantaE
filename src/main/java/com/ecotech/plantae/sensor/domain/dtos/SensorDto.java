package com.ecotech.plantae.sensor.domain.dtos;

public record SensorDto(
        String id,
        String type,
        String status,
        String plantId,
        String ownerId,
        String lastReadingAt,
        String createdAt,
        String updatedAt
) {
}
