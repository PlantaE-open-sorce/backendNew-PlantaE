package com.ecotech.plantae.plant.domain.repositories;

import com.ecotech.plantae.plant.domain.entities.Plant;
import com.ecotech.plantae.plant.domain.repositories.models.PagedResult;
import com.ecotech.plantae.plant.domain.repositories.models.PlantSearchCriteria;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;

import java.util.Optional;

public interface PlantRepository {
    Plant save(Plant plant);
    Optional<Plant> findById(PlantId id);
    Optional<Plant> findByIdAndOwner(PlantId id, String ownerId);
    PagedResult<Plant> search(PlantSearchCriteria criteria);
}
