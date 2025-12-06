package com.ecotech.plantae.alert.infrastructure.persistence.mappers;

import com.ecotech.plantae.alert.domain.entities.Alert;
import com.ecotech.plantae.alert.domain.valueobjects.AlertId;
import com.ecotech.plantae.alert.domain.valueobjects.AlertStatus;
import com.ecotech.plantae.alert.domain.valueobjects.AlertType;
import com.ecotech.plantae.alert.infrastructure.persistence.AlertJpaEntity;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorId;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@SuppressWarnings("null")
public class AlertJpaMapper {

    public @NonNull AlertJpaEntity toJpa(@NonNull Alert alert) {
        Map<String, String> metadata = new HashMap<>();
        alert.getMetadata().forEach((k, v) -> metadata.put(k, String.valueOf(v)));
        return new AlertJpaEntity(
                alert.getId().value(),
                alert.getPlantId().value(),
                alert.getSensorId() != null ? alert.getSensorId().value() : null,
                alert.getOwnerId(),
                alert.getType().name(),
                alert.getStatus().name(),
                alert.getOccurredAt(),
                alert.getResolvedAt(),
                metadata
        );
    }

    public @NonNull Alert toDomain(@NonNull AlertJpaEntity entity) {
        Map<String, Object> metadata = new HashMap<>(entity.getMetadata());
        return Alert.restore(
                AlertId.of(entity.getId()),
                PlantId.of(entity.getPlantId()),
                entity.getSensorId() != null ? SensorId.of(entity.getSensorId()) : null,
                entity.getOwnerId(),
                AlertType.valueOf(entity.getType()),
                AlertStatus.valueOf(entity.getStatus()),
                entity.getOccurredAt(),
                entity.getResolvedAt(),
                metadata
        );
    }
}
