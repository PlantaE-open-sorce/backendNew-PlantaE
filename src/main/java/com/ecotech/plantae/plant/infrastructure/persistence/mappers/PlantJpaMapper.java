package com.ecotech.plantae.plant.infrastructure.persistence.mappers;

import com.ecotech.plantae.plant.domain.entities.Plant;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;
import com.ecotech.plantae.plant.domain.valueobjects.PlantStatus;
import com.ecotech.plantae.plant.infrastructure.persistence.PlantJpaEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("null")
public class PlantJpaMapper {

    public @NonNull PlantJpaEntity toJpa(@NonNull Plant plant) {
        return new PlantJpaEntity(
                plant.getId().value(),
                plant.getOwnerId(),
                plant.getName(),
                plant.getSpecies(),
                plant.getLocation(),
                plant.getSensorId(),
                plant.getStatus().name(),
                plant.hasAlerts(),
                plant.getCreatedAt(),
                plant.getUpdatedAt(),
                plant.getDeletedAt());
    }

    public @NonNull Plant toDomain(@NonNull PlantJpaEntity entity) {
        return Plant.restore(
                PlantId.of(entity.getId()),
                entity.getOwnerId(),
                entity.getName(),
                entity.getSpecies(),
                entity.getLocation(),
                entity.getSensorId(),
                PlantStatus.valueOf(entity.getStatus()),
                entity.isHasAlerts(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt());
    }
}
