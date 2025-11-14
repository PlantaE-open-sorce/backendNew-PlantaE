package com.ecotech.plantae.sensor.application.handlers;

import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;
import com.ecotech.plantae.sensor.application.commands.RegisterSensorCommand;
import com.ecotech.plantae.sensor.domain.dtos.SensorDto;
import com.ecotech.plantae.sensor.domain.entities.Sensor;
import com.ecotech.plantae.sensor.domain.repositories.SensorRepository;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorType;

public class RegisterSensorHandler {

    private final SensorRepository sensorRepository;
    private final PlantRepository plantRepository;

    public RegisterSensorHandler(SensorRepository sensorRepository, PlantRepository plantRepository) {
        this.sensorRepository = sensorRepository;
        this.plantRepository = plantRepository;
    }

    public SensorDto handle(RegisterSensorCommand command) {
        String owner = command.ownerId() != null ? command.ownerId().trim() : null;
        if (owner == null || owner.isEmpty()) {
            throw new IllegalArgumentException("Owner id is required");
        }
        Sensor sensor = Sensor.register(SensorType.valueOf(command.type().toUpperCase()), owner);
        if (command.plantId() != null && !command.plantId().isBlank()) {
            PlantId plantId = PlantId.of(command.plantId());
            var plant = plantRepository.findById(plantId).orElseThrow(() -> new IllegalArgumentException("Plant not found"));
            sensor.linkToPlant(plantId);
            plant.assignSensor(sensor.getId().value());
            plantRepository.save(plant);
        }
        Sensor saved = sensorRepository.save(sensor);
        return SensorDtoMapper.toDto(saved);
    }
}
