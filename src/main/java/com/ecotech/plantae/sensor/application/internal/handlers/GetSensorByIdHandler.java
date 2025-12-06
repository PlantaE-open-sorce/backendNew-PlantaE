package com.ecotech.plantae.sensor.application.internal.handlers;

import com.ecotech.plantae.sensor.application.internal.queries.GetSensorByIdQuery;
import com.ecotech.plantae.sensor.domain.entities.Sensor;
import com.ecotech.plantae.sensor.domain.repositories.SensorRepository;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorId;

public class GetSensorByIdHandler {

    private final SensorRepository sensorRepository;

    public GetSensorByIdHandler(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public Sensor handle(GetSensorByIdQuery query) {
        return sensorRepository.findById(SensorId.of(query.sensorId()))
                .orElse(null);
    }
}
