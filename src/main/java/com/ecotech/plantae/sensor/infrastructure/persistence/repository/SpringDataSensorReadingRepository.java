package com.ecotech.plantae.sensor.infrastructure.persistence.repository;

import com.ecotech.plantae.sensor.infrastructure.persistence.SensorReadingJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface SpringDataSensorReadingRepository extends JpaRepository<SensorReadingJpaEntity, String> {

    Page<SensorReadingJpaEntity> findBySensorIdAndTimestampBetweenAndMetricContainingIgnoreCase(
            String sensorId,
            Instant from,
            Instant to,
            String metric,
            Pageable pageable);

    @Query("select r.sensorId as sensorId, count(r) as total from SensorReadingJpaEntity r " +
            "where (:from is null or r.timestamp >= :from) and (:to is null or r.timestamp <= :to) " +
            "group by r.sensorId order by count(r) desc")
    List<Object[]> topActive(@Param("from") Instant from, @Param("to") Instant to, Pageable pageable);
}
