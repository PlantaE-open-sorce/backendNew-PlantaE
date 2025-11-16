package com.ecotech.plantae.alert.application.handlers;

import com.ecotech.plantae.alert.application.commands.ResolveAlertCommand;
import com.ecotech.plantae.alert.domain.repositories.AlertRepository;
import com.ecotech.plantae.alert.domain.valueobjects.AlertId;
import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;

public class ResolveAlertHandler {

    private final AlertRepository alertRepository;
    private final PlantRepository plantRepository;

    public ResolveAlertHandler(AlertRepository alertRepository, PlantRepository plantRepository) {
        this.alertRepository = alertRepository;
        this.plantRepository = plantRepository;
    }

    public void handle(ResolveAlertCommand command) {
        var alert = alertRepository.findById(AlertId.of(command.alertId()))
                .orElseThrow(() -> new IllegalArgumentException("Alert not found"));
        alert.resolve();
        alertRepository.save(alert);
        long remaining = alertRepository.countActiveByPlant(alert.getPlantId());
        if (remaining == 0) {
            plantRepository.findById(alert.getPlantId()).ifPresent(plant -> {
                plant.setHasAlerts(false);
                plantRepository.save(plant);
            });
        }
    }
}
