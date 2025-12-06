package com.ecotech.plantae.plant.application.internal.queries;

public record SearchPlantsQuery(
        String ownerId,
        String name,
        String species,
        String location,
        String status,
        String createdFrom,
        String createdTo,
        Boolean hasAlerts,
        String sensorId,
        String sort,
        int page,
        int size
) {}
