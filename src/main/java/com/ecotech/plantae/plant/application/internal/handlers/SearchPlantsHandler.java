package com.ecotech.plantae.plant.application.internal.handlers;

import com.ecotech.plantae.plant.application.internal.queries.SearchPlantsQuery;
import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import com.ecotech.plantae.plant.domain.repositories.models.PagedResult;
import com.ecotech.plantae.plant.domain.repositories.models.PlantSearchCriteria;
import com.ecotech.plantae.plant.domain.valueobjects.PlantStatus;

import java.time.Instant;

public class SearchPlantsHandler {

    private final PlantRepository plantRepository;

    public SearchPlantsHandler(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    public PagedResult<com.ecotech.plantae.plant.domain.entities.Plant> handle(SearchPlantsQuery query) {
        PlantSearchCriteria criteria = new PlantSearchCriteria(
                query.ownerId(),
                query.name(),
                query.species(),
                query.location(),
                query.status() != null ? PlantStatus.valueOf(query.status().toUpperCase()) : null,
                parseInstant(query.createdFrom()),
                parseInstant(query.createdTo()),
                query.hasAlerts(),
                query.sensorId(),
                query.sort(),
                query.page(),
                query.size());
        return plantRepository.search(criteria);
    }

    private Instant parseInstant(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Instant.parse(value);
    }
}
