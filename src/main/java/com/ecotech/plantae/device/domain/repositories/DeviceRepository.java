package com.ecotech.plantae.device.domain.repositories;

import com.ecotech.plantae.device.domain.entities.Device;
import com.ecotech.plantae.device.domain.valueobjects.DeviceId;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository {
    Device save(Device device);
    Optional<Device> findById(DeviceId id);
    boolean existsById(DeviceId id);
    List<Device> findByOwnerId(String ownerId);
}
