package com.ecotech.plantae.alert.domain.dtos;

public record AlertDto(
        String id,
        String plantId,
        String sensorId,
        String type,
        String status,
        String message,
        String occurredAt,
        String resolvedAt,
        double value,
        String metric
) {}
