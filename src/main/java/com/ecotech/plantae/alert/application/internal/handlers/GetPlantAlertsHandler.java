package com.ecotech.plantae.alert.application.internal.handlers;

import com.ecotech.plantae.alert.application.internal.queries.GetPlantAlertsQuery;
import com.ecotech.plantae.alert.domain.entities.Alert;
import com.ecotech.plantae.alert.domain.repositories.AlertRepository;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;

import java.util.List;

public class GetPlantAlertsHandler {

    private final AlertRepository alertRepository;

    public GetPlantAlertsHandler(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public List<Alert> handle(GetPlantAlertsQuery query) {
        return alertRepository.findByPlant(PlantId.of(query.plantId()), query.page(), query.size()).stream()
                .filter(alert -> alert.getOwnerId().equals(query.ownerId()))
                .toList();
    }
}
