package com.ecotech.plantae.plant.application.handlers;

import com.ecotech.plantae.plant.domain.dtos.PlantDto;
import com.ecotech.plantae.plant.domain.entities.Plant;

final class PlantDtoMapper {

    private PlantDtoMapper() {
    }

    static PlantDto toDto(Plant plant) {
        return new PlantDto(
                plant.getId().value(),
                plant.getOwnerId(),
                plant.getName(),
                plant.getSpecies(),
                plant.getStatus().name(),
                plant.getDeviceId() != null ? plant.getDeviceId().value() : null,
                plant.getSensorId(),
                plant.hasAlerts(),
                plant.getCreatedAt().toString(),
                plant.getUpdatedAt().toString()
        );
    }
}
