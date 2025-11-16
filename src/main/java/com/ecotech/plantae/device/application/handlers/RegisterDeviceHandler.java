package com.ecotech.plantae.device.application.handlers;

import com.ecotech.plantae.device.application.commands.RegisterDeviceCommand;
import com.ecotech.plantae.device.domain.dtos.DeviceDto;
import com.ecotech.plantae.device.domain.entities.Device;
import com.ecotech.plantae.device.domain.repositories.DeviceRepository;
import com.ecotech.plantae.device.domain.valueobjects.DeviceId;
import com.ecotech.plantae.device.domain.valueobjects.OwnerId;

public class RegisterDeviceHandler {

    private final DeviceRepository deviceRepository;

    public RegisterDeviceHandler(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public DeviceDto handle(RegisterDeviceCommand command) {
        DeviceId deviceId = DeviceId.of(command.deviceId());
        if (deviceRepository.existsById(deviceId)) {
            throw new IllegalStateException("Device already registered");
        }
        Device device = Device.register(deviceId, OwnerId.of(command.ownerId()), command.hwModel(), command.secret());
        return DeviceDtoMapper.toDto(deviceRepository.save(device));
    }
}
