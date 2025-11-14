package com.ecotech.plantae.device.application.handlers;

import com.ecotech.plantae.device.application.commands.DeactivateDeviceCommand;
import com.ecotech.plantae.device.domain.dtos.DeviceDto;
import com.ecotech.plantae.device.domain.entities.Device;
import com.ecotech.plantae.device.domain.repositories.DeviceRepository;
import com.ecotech.plantae.device.domain.valueobjects.DeviceId;

public class DeactivateDeviceHandler {

    private final DeviceRepository deviceRepository;

    public DeactivateDeviceHandler(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public DeviceDto handle(DeactivateDeviceCommand command) {
        Device device = deviceRepository.findById(DeviceId.of(command.deviceId()))
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));
        device.deactivate();
        return DeviceDtoMapper.toDto(deviceRepository.save(device));
    }
}
