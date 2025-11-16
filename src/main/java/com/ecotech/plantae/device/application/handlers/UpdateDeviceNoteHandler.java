package com.ecotech.plantae.device.application.handlers;

import com.ecotech.plantae.device.application.commands.UpdateDeviceNoteCommand;
import com.ecotech.plantae.device.domain.dtos.DeviceDto;
import com.ecotech.plantae.device.domain.entities.Device;
import com.ecotech.plantae.device.domain.repositories.DeviceRepository;
import com.ecotech.plantae.device.domain.valueobjects.DeviceId;

public class UpdateDeviceNoteHandler {

    private final DeviceRepository deviceRepository;

    public UpdateDeviceNoteHandler(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public DeviceDto handle(UpdateDeviceNoteCommand command) {
        Device device = deviceRepository.findById(DeviceId.of(command.deviceId()))
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));
        device.updateNote(command.note());
        return DeviceDtoMapper.toDto(deviceRepository.save(device));
    }
}
