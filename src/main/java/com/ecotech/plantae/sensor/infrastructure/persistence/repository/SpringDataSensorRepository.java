package com.ecotech.plantae.sensor.infrastructure.persistence.repository;

import com.ecotech.plantae.sensor.infrastructure.persistence.SensorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SpringDataSensorRepository extends JpaRepository<SensorJpaEntity, String>, JpaSpecificationExecutor<SensorJpaEntity> {
}
