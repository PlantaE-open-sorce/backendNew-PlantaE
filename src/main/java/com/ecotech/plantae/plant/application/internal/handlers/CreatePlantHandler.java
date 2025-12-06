package com.ecotech.plantae.plant.application.internal.handlers;

import com.ecotech.plantae.plant.application.internal.commands.CreatePlantCommand;
import com.ecotech.plantae.plant.domain.entities.Plant;
import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import com.ecotech.plantae.plant.application.internal.services.SpeciesCatalogService;

public class CreatePlantHandler {

    private final PlantRepository plantRepository;
    private final SpeciesCatalogService speciesCatalogService;

    public CreatePlantHandler(PlantRepository plantRepository, SpeciesCatalogService speciesCatalogService) {
        this.plantRepository = plantRepository;
        this.speciesCatalogService = speciesCatalogService;
    }

    public Plant handle(CreatePlantCommand command) {
        validateSpecies(command.species());
        Plant plant = Plant.create(command.ownerId(), command.name(), command.species(), command.location(),
                normalizeId(command.sensorId()));
        return plantRepository.save(plant);
    }

    private String normalizeId(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private void validateSpecies(String species) {
        if (!speciesCatalogService.isValidSpecies(species)) {
            throw new IllegalArgumentException("Invalid species");
        }
    }
}
