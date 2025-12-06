package com.ecotech.plantae.sensor.application.internal.handlers;

import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;
import com.ecotech.plantae.sensor.application.internal.commands.RegisterSensorCommand;
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

    public Sensor handle(RegisterSensorCommand command) {
        String owner = command.ownerId() != null ? command.ownerId().trim() : null;
        if (owner == null || owner.isEmpty()) {
            throw new IllegalArgumentException("Owner id is required");
        }
        PlantId plantId = PlantId.of(requirePlant(command.plantId()));
        var plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new IllegalArgumentException("Plant not found"));
        Sensor sensor = Sensor.register(SensorType.valueOf(command.type().toUpperCase()), owner, plantId);
        plant.assignSensor(sensor.getId().value());
        plantRepository.save(plant);
        return sensorRepository.save(sensor);
    }

    private String requirePlant(String plantId) {
        if (plantId == null || plantId.isBlank()) {
            throw new IllegalArgumentException("Plant id is required");
        }
        return plantId.trim();
    }
}
