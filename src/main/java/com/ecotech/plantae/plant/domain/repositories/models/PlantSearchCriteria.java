package com.ecotech.plantae.plant.domain.repositories.models;

import com.ecotech.plantae.plant.domain.valueobjects.PlantStatus;

import java.time.Instant;

public record PlantSearchCriteria(
        String ownerId,
        String name,
        String species,
        String location,
        PlantStatus status,
        Instant createdFrom,
        Instant createdTo,
        Boolean hasAlerts,
        String sensorId,
        String sort,
        int page,
        int size
) {
}
