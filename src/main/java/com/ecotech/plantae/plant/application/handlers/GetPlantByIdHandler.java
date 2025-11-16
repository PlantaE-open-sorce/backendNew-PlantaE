package com.ecotech.plantae.plant.application.handlers;

import com.ecotech.plantae.plant.application.queries.GetPlantByIdQuery;
import com.ecotech.plantae.plant.domain.dtos.PlantDto;
import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;

public class GetPlantByIdHandler {

    private final PlantRepository plantRepository;

    public GetPlantByIdHandler(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    public PlantDto handle(GetPlantByIdQuery query) {
        return plantRepository.findByIdAndOwner(PlantId.of(query.plantId()), query.ownerId())
                .map(PlantDtoMapper::toDto)
                .orElse(null);
    }
}
