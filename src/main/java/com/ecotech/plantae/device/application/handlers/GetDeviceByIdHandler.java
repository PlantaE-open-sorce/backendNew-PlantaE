package com.ecotech.plantae.device.application.handlers;

import com.ecotech.plantae.device.application.queries.GetDeviceByIdQuery;
import com.ecotech.plantae.device.domain.dtos.DeviceDto;
import com.ecotech.plantae.device.domain.repositories.DeviceRepository;
import com.ecotech.plantae.device.domain.valueobjects.DeviceId;

public class GetDeviceByIdHandler {

    private final DeviceRepository deviceRepository;

    public GetDeviceByIdHandler(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public DeviceDto handle(GetDeviceByIdQuery query) {
        return deviceRepository.findById(DeviceId.of(query.deviceId()))
                .map(DeviceDtoMapper::toDto)
                .orElse(null);
    }
}
