package com.ecotech.plantae.device.application.handlers;

import com.ecotech.plantae.device.domain.dtos.DeviceDto;
import com.ecotech.plantae.device.domain.entities.Device;

final class DeviceDtoMapper {

    private DeviceDtoMapper() {
    }

    static DeviceDto toDto(Device device) {
        return new DeviceDto(
                device.getId().value(),
                device.getOwnerId().value(),
                device.getHwModel(),
                device.isActive(),
                device.getNote()
        );
    }
}
