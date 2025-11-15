package com.ecotech.plantae.plant.infrastructure.persistence.mappers;

import com.ecotech.plantae.plant.domain.entities.Plant;
import com.ecotech.plantae.plant.domain.valueobjects.DeviceId;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;
import com.ecotech.plantae.plant.domain.valueobjects.PlantStatus;
import com.ecotech.plantae.plant.infrastructure.persistence.PlantJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PlantJpaMapper {

    public PlantJpaEntity toJpa(Plant plant) {
        return new PlantJpaEntity(
                plant.getId().value(),
                plant.getOwnerId(),
                plant.getName(),
                plant.getSpecies(),
                plant.getDeviceId() != null ? plant.getDeviceId().value() : null,
                plant.getSensorId(),
                plant.getStatus().name(),
                plant.hasAlerts(),
                plant.getCreatedAt(),
                plant.getUpdatedAt(),
                plant.getDeletedAt()
        );
    }

    public Plant toDomain(PlantJpaEntity entity) {
        return Plant.restore(
                PlantId.of(entity.getId()),
                entity.getOwnerId(),
                entity.getName(),
                entity.getSpecies(),
                DeviceId.of(entity.getDeviceId()),
                entity.getSensorId(),
                PlantStatus.valueOf(entity.getStatus()),
                entity.isHasAlerts(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }
}
