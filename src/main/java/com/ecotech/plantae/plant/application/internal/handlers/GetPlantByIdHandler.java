package com.ecotech.plantae.plant.application.internal.handlers;

import com.ecotech.plantae.plant.application.internal.queries.GetPlantByIdQuery;
import com.ecotech.plantae.plant.domain.entities.Plant;
import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;

public class GetPlantByIdHandler {

    private final PlantRepository plantRepository;

    public GetPlantByIdHandler(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    public Plant handle(GetPlantByIdQuery query) {
        return plantRepository.findByIdAndOwner(PlantId.of(query.plantId()), query.ownerId())
                .orElse(null);
    }
}
