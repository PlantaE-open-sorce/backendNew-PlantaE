package com.ecotech.plantae.alert.application.internal.handlers;

import com.ecotech.plantae.alert.application.internal.commands.RaiseAlertCommand;
import com.ecotech.plantae.alert.domain.entities.Alert;
import com.ecotech.plantae.alert.domain.repositories.AlertRepository;
import com.ecotech.plantae.alert.domain.services.AlertNotificationService;
import com.ecotech.plantae.alert.domain.valueobjects.AlertType;
import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorId;
import com.ecotech.plantae.shared.infrastructure.realtime.AlertEventPayload;
import com.ecotech.plantae.shared.infrastructure.realtime.RealtimeEmitter;

public class RaiseAlertHandler {

    private final AlertRepository alertRepository;
    private final PlantRepository plantRepository;
    private final AlertNotificationService notificationService;
    private final RealtimeEmitter realtimeEmitter;

    public RaiseAlertHandler(AlertRepository alertRepository,
                             PlantRepository plantRepository,
                             AlertNotificationService notificationService,
                             RealtimeEmitter realtimeEmitter) {
        this.alertRepository = alertRepository;
        this.plantRepository = plantRepository;
        this.notificationService = notificationService;
        this.realtimeEmitter = realtimeEmitter;
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
        realtimeEmitter.publishAlert(alert.getOwnerId(), new AlertEventPayload(
                alert.getId().value(),
                alert.getPlantId().value(),
                alert.getSensorId() != null ? alert.getSensorId().value() : null,
                alert.getType().name(),
                alert.getStatus().name(),
                alert.getOccurredAt().toString()
        ));
    }
}
