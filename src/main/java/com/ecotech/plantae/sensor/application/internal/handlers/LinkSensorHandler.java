package com.ecotech.plantae.sensor.application.internal.handlers;

import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;
import com.ecotech.plantae.sensor.application.internal.commands.LinkSensorCommand;
import com.ecotech.plantae.sensor.domain.repositories.SensorRepository;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorId;

public class LinkSensorHandler {

    private final SensorRepository sensorRepository;
    private final PlantRepository plantRepository;

    public LinkSensorHandler(SensorRepository sensorRepository, PlantRepository plantRepository) {
        this.sensorRepository = sensorRepository;
        this.plantRepository = plantRepository;
    }

    public void handle(LinkSensorCommand command) {
        var sensor = sensorRepository.findById(SensorId.of(command.sensorId()))
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));
        var plant = plantRepository.findById(PlantId.of(command.plantId()))
                .orElseThrow(() -> new IllegalArgumentException("Plant not found"));
        sensor.linkToPlant(PlantId.of(command.plantId()));
        plant.assignSensor(sensor.getId().value());
        plantRepository.save(plant);
        sensorRepository.save(sensor);
    }
}
