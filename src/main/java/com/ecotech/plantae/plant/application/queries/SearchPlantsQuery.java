package com.ecotech.plantae.plant.application.queries;

public record SearchPlantsQuery(
        String ownerId,
        String name,
        String species,
        String status,
        String createdFrom,
        String createdTo,
        Boolean hasAlerts,
        String sensorId,
        String sort,
        int page,
        int size
) {}
