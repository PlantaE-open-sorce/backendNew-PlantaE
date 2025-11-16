package com.ecotech.plantae.sensor.application.handlers;

import com.ecotech.plantae.sensor.application.commands.DeactivateSensorCommand;
import com.ecotech.plantae.sensor.domain.repositories.SensorRepository;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorId;

public class DeactivateSensorHandler {

    private final SensorRepository sensorRepository;

    public DeactivateSensorHandler(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public void handle(DeactivateSensorCommand command) {
        var sensor = sensorRepository.findById(SensorId.of(command.sensorId()))
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));
        sensor.deactivate();
        sensorRepository.save(sensor);
    }
}
