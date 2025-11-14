package com.ecotech.plantae.plant.application.handlers;

import com.ecotech.plantae.plant.application.commands.CreatePlantCommand;
import com.ecotech.plantae.plant.domain.dtos.PlantDto;
import com.ecotech.plantae.plant.domain.entities.Plant;
import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import com.ecotech.plantae.plant.domain.valueobjects.DeviceId;

public class CreatePlantHandler {

    private final PlantRepository plantRepository;

    public CreatePlantHandler(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    public PlantDto handle(CreatePlantCommand command) {
        DeviceId deviceId = DeviceId.of(normalizeId(command.deviceId()));
        Plant plant = Plant.create(command.ownerId(), command.name(), command.species(), deviceId, normalizeId(command.sensorId()));
        return PlantDtoMapper.toDto(plantRepository.save(plant));
    }

    private String normalizeId(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
