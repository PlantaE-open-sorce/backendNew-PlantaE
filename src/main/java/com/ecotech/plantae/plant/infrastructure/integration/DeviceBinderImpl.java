package com.ecotech.plantae.plant.infrastructure.integration;

import com.ecotech.plantae.plant.application.ports.DeviceBinder;
import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import com.ecotech.plantae.plant.domain.valueobjects.DeviceId;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;
import org.springframework.stereotype.Component;

@Component
public class DeviceBinderImpl implements DeviceBinder {

    private final PlantRepository plantRepository;

    public DeviceBinderImpl(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    @Override
    public void bind(String deviceId, String plantId) {
        var plant = plantRepository.findById(PlantId.of(plantId))
                .orElseThrow(() -> new IllegalArgumentException("Plant not found"));
        plant.assignDevice(DeviceId.of(deviceId));
        plantRepository.save(plant);
    }
}
