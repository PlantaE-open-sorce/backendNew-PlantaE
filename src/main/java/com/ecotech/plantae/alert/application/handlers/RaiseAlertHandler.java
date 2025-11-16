package com.ecotech.plantae.alert.application.handlers;

import com.ecotech.plantae.alert.application.commands.RaiseAlertCommand;
import com.ecotech.plantae.alert.domain.entities.Alert;
import com.ecotech.plantae.alert.domain.repositories.AlertRepository;
import com.ecotech.plantae.alert.domain.services.AlertNotificationService;
import com.ecotech.plantae.alert.domain.valueobjects.AlertType;
import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorId;

public class RaiseAlertHandler {

    private final AlertRepository alertRepository;
    private final PlantRepository plantRepository;
    private final AlertNotificationService notificationService;

    public RaiseAlertHandler(AlertRepository alertRepository,
                             PlantRepository plantRepository,
                             AlertNotificationService notificationService) {
        this.alertRepository = alertRepository;
        this.plantRepository = plantRepository;
        this.notificationService = notificationService;
    }

    public void handle(RaiseAlertCommand command) {
        var plant = plantRepository.findById(PlantId.of(command.plantId()))
                .orElseThrow(() -> new IllegalArgumentException("Plant not found"));
        Alert alert = Alert.raise(
                plant.getId(),
                SensorId.of(command.sensorId()),
                command.ownerId(),
                AlertType.valueOf(command.type().toUpperCase())
        );
        alert.addMetadata("metric", command.metric());
        alert.addMetadata("value", command.value());
        alert.addMetadata("threshold", command.threshold());
        alertRepository.save(alert);
        plant.setHasAlerts(true);
        plantRepository.save(plant);
        notificationService.notify(alert, command.acceptLanguage());
    }
}
