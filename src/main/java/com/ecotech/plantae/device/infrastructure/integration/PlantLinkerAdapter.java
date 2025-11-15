package com.ecotech.plantae.device.infrastructure.integration;

import com.ecotech.plantae.device.application.ports.PlantLinker;
import com.ecotech.plantae.plant.application.ports.DeviceBinder;
import org.springframework.stereotype.Component;

@Component
public class PlantLinkerAdapter implements PlantLinker {

    private final DeviceBinder deviceBinder;

    public PlantLinkerAdapter(DeviceBinder deviceBinder) {
        this.deviceBinder = deviceBinder;
    }

    @Override
    public void link(String deviceId, String plantId) {
        deviceBinder.bind(deviceId, plantId);
    }
}
