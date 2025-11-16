package com.ecotech.plantae.sensor.application.handlers;

import com.ecotech.plantae.sensor.application.queries.GetSensorByIdQuery;
import com.ecotech.plantae.sensor.domain.dtos.SensorDto;
import com.ecotech.plantae.sensor.domain.repositories.SensorRepository;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorId;

public class GetSensorByIdHandler {

    private final SensorRepository sensorRepository;

    public GetSensorByIdHandler(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public SensorDto handle(GetSensorByIdQuery query) {
        return sensorRepository.findById(SensorId.of(query.sensorId()))
                .map(SensorDtoMapper::toDto)
                .orElse(null);
    }
}
