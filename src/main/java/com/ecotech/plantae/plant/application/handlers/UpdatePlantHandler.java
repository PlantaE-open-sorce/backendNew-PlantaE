package com.ecotech.plantae.plant.application.handlers;

import com.ecotech.plantae.plant.application.commands.UpdatePlantCommand;
import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import com.ecotech.plantae.plant.domain.valueobjects.DeviceId;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;
import com.ecotech.plantae.plant.domain.valueobjects.PlantStatus;

public class UpdatePlantHandler {

    private final PlantRepository plantRepository;

    public UpdatePlantHandler(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    public void handle(UpdatePlantCommand command) {
        var plant = plantRepository.findByIdAndOwner(PlantId.of(command.plantId()), command.ownerId())
                .orElseThrow(() -> new IllegalArgumentException("Plant not found"));
        plant.updateDetails(
                command.name(),
                command.species(),
                normalizeId(command.deviceId()),
                command.sensorId(),
                command.status() != null ? PlantStatus.valueOf(command.status().toUpperCase()) : null
        );
        plantRepository.save(plant);
    }

    private String normalizeId(String deviceId) {
        if (deviceId == null) {
            return null;
        }
        String trimmed = deviceId.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
