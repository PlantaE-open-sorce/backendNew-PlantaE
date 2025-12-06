package com.ecotech.plantae.plant.application.internal.handlers;

import com.ecotech.plantae.plant.application.internal.commands.DeletePlantCommand;
import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;

public class DeletePlantHandler {

    private final PlantRepository plantRepository;

    public DeletePlantHandler(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    public void handle(DeletePlantCommand command) {
        var plant = plantRepository.findByIdAndOwner(PlantId.of(command.plantId()), command.ownerId())
                .orElseThrow(() -> new IllegalArgumentException("Plant not found"));
        plant.markDeleted();
        plantRepository.save(plant);
    }
}
