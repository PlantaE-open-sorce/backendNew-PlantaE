package com.ecotech.plantae.device.application.handlers;

import com.ecotech.plantae.device.application.commands.LinkDeviceToPlantCommand;
import com.ecotech.plantae.device.application.ports.PlantLinker;
import com.ecotech.plantae.device.domain.repositories.DeviceRepository;
import com.ecotech.plantae.device.domain.valueobjects.DeviceId;

public class LinkDeviceToPlantHandler {

    private final DeviceRepository deviceRepository;
    private final PlantLinker plantLinker;

    public LinkDeviceToPlantHandler(DeviceRepository deviceRepository, PlantLinker plantLinker) {
        this.deviceRepository = deviceRepository;
        this.plantLinker = plantLinker;
    }

    public void handle(LinkDeviceToPlantCommand command) {
        deviceRepository.findById(DeviceId.of(command.deviceId()))
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));
        plantLinker.link(command.deviceId(), command.plantId());
    }
}
