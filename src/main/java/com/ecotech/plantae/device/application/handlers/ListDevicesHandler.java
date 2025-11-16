package com.ecotech.plantae.device.application.handlers;

import com.ecotech.plantae.device.application.queries.ListDevicesQuery;
import com.ecotech.plantae.device.domain.dtos.DeviceDto;
import com.ecotech.plantae.device.domain.repositories.DeviceRepository;

import java.util.List;

public class ListDevicesHandler {

    private final DeviceRepository deviceRepository;

    public ListDevicesHandler(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<DeviceDto> handle(ListDevicesQuery query) {
        return deviceRepository.findByOwnerId(query.ownerId()).stream()
                .filter(device -> !query.activeOnly() || device.isActive())
                .map(DeviceDtoMapper::toDto)
                .toList();
    }
}
