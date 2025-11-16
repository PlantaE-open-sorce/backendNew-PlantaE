package com.ecotech.plantae.device.infrastructure.persistence.repository;

import com.ecotech.plantae.device.infrastructure.persistence.DeviceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataDeviceRepository extends JpaRepository<DeviceJpaEntity, String> {
    List<DeviceJpaEntity> findByOwnerId(String ownerId);
}
