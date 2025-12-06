package com.ecotech.plantae.plant.application.internal.handlers;

import com.ecotech.plantae.plant.application.internal.commands.UpdatePlantCommand;
import com.ecotech.plantae.plant.application.internal.services.SpeciesCatalogService;
import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;
import com.ecotech.plantae.plant.domain.valueobjects.PlantStatus;

public class UpdatePlantHandler {

    private final PlantRepository plantRepository;
    private final SpeciesCatalogService speciesCatalogService;

    public UpdatePlantHandler(PlantRepository plantRepository, SpeciesCatalogService speciesCatalogService) {
        this.plantRepository = plantRepository;
        this.speciesCatalogService = speciesCatalogService;
    }

    public void handle(UpdatePlantCommand command) {
        var plant = plantRepository.findByIdAndOwner(PlantId.of(command.plantId()), command.ownerId())
                .orElseThrow(() -> new IllegalArgumentException("Plant not found"));
        if (command.species() != null && !command.species().isBlank()
                && !speciesCatalogService.isValidSpecies(command.species())) {
            throw new IllegalArgumentException("Invalid species");
        }
        plant.updateDetails(
                command.name(),
                command.species(),
                command.sensorId(),
                command.status() != null ? PlantStatus.valueOf(command.status().toUpperCase()) : null,
                command.location());
        plantRepository.save(plant);
    }
}
