package com.ecotech.plantae.sensor.application.handlers;

import com.ecotech.plantae.sensor.application.commands.IngestSensorReadingCommand;
import com.ecotech.plantae.sensor.domain.entities.SensorReading;
import com.ecotech.plantae.sensor.domain.repositories.SensorReadingRepository;
import com.ecotech.plantae.sensor.domain.repositories.SensorRepository;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorId;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorMetric;

import java.time.Instant;

public class IngestSensorReadingHandler {

    private final SensorRepository sensorRepository;
    private final SensorReadingRepository sensorReadingRepository;

    public IngestSensorReadingHandler(SensorRepository sensorRepository, SensorReadingRepository sensorReadingRepository) {
        this.sensorRepository = sensorRepository;
        this.sensorReadingRepository = sensorReadingRepository;
    }

    public void handle(IngestSensorReadingCommand command) {
        var sensor = sensorRepository.findById(SensorId.of(command.sensorId()))
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));
        Instant timestamp = command.timestamp() != null && !command.timestamp().isBlank()
                ? Instant.parse(command.timestamp())
                : Instant.now();
        SensorReading reading = new SensorReading(sensor.getId(), timestamp,
                SensorMetric.from(command.metric()), command.value(), command.quality());
        sensorReadingRepository.save(reading);
        sensor.markReading(timestamp);
        sensorRepository.save(sensor);
    }
}
