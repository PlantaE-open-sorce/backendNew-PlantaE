package com.ecotech.plantae.sensor.infrastructure.persistence.mappers;

import com.ecotech.plantae.plant.domain.valueobjects.PlantId;
import com.ecotech.plantae.sensor.domain.entities.Sensor;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorId;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorStatus;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorType;
import com.ecotech.plantae.sensor.infrastructure.persistence.SensorJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class SensorJpaMapper {

    public SensorJpaEntity toJpa(Sensor sensor) {
        return new SensorJpaEntity(
                sensor.getId().value(),
                sensor.getType().name(),
                sensor.getStatus().name(),
                sensor.getPlantId() != null ? sensor.getPlantId().value() : null,
                sensor.getOwnerId(),
                sensor.getLastReadingAt(),
                sensor.getCreatedAt(),
                sensor.getUpdatedAt()
        );
    }

    public Sensor toDomain(SensorJpaEntity entity) {
        return Sensor.restore(
                SensorId.of(entity.getId()),
                SensorType.valueOf(entity.getType()),
                SensorStatus.valueOf(entity.getStatus()),
                entity.getPlantId() != null ? PlantId.of(entity.getPlantId()) : null,
                entity.getOwnerId(),
                entity.getLastReadingAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
