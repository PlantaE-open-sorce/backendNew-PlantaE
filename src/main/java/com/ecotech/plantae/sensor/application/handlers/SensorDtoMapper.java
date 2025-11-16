package com.ecotech.plantae.sensor.application.handlers;

import com.ecotech.plantae.sensor.domain.dtos.SensorDto;
import com.ecotech.plantae.sensor.domain.entities.Sensor;

final class SensorDtoMapper {

    private SensorDtoMapper() {}

    static SensorDto toDto(Sensor sensor) {
        return new SensorDto(
                sensor.getId().value(),
                sensor.getType().name(),
                sensor.getStatus().name(),
                sensor.getPlantId() != null ? sensor.getPlantId().value() : null,
                sensor.getOwnerId(),
                sensor.getLastReadingAt() != null ? sensor.getLastReadingAt().toString() : null,
                sensor.getCreatedAt().toString(),
                sensor.getUpdatedAt().toString()
        );
    }
}
