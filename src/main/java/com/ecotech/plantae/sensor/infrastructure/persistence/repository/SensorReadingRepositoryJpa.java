package com.ecotech.plantae.sensor.infrastructure.persistence.repository;

import com.ecotech.plantae.sensor.domain.dtos.SensorActivityDto;
import com.ecotech.plantae.sensor.domain.entities.SensorReading;
import com.ecotech.plantae.sensor.domain.models.PagedResult;
import com.ecotech.plantae.sensor.domain.models.SensorReadingSearchCriteria;
import com.ecotech.plantae.sensor.domain.repositories.SensorReadingRepository;
import com.ecotech.plantae.sensor.infrastructure.persistence.SensorReadingJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public class SensorReadingRepositoryJpa implements SensorReadingRepository {

    private final SpringDataSensorReadingRepository springDataRepository;

    public SensorReadingRepositoryJpa(SpringDataSensorReadingRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public void save(SensorReading reading) {
        springDataRepository.save(new SensorReadingJpaEntity(
                reading.getSensorId().value(),
                reading.getTimestamp(),
                reading.getMetric().apiName(),
                reading.getValue(),
                reading.getQuality()
        ));
    }

    @Override
    public PagedResult<SensorReading> search(SensorReadingSearchCriteria criteria) {
        Pageable pageable = PageRequest.of(Math.max(criteria.page(), 0), Math.max(criteria.size(), 1));
        Instant from = criteria.from();
        Instant to = criteria.to();
        String metric = criteria.metric() != null ? criteria.metric().name() : "";
        Page<SensorReadingJpaEntity> page = springDataRepository
                .findBySensorIdAndTimestampBetweenAndMetricContainingIgnoreCase(
                        criteria.sensorId(),
                        from != null ? from : Instant.EPOCH,
                        to != null ? to : Instant.now(),
                        metric,
                        pageable
                );
        return new PagedResult<>(
                page.map(entity -> new SensorReading(
                        com.ecotech.plantae.sensor.domain.valueobjects.SensorId.of(entity.getSensorId()),
                        entity.getTimestamp(),
                        com.ecotech.plantae.sensor.domain.valueobjects.SensorMetric.from(entity.getMetric()),
                        entity.getValue(),
                        entity.getQuality()
                )).toList(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages()
        );
    }

    @Override
    public List<SensorActivityDto> mostActive(Instant from, Instant to, int top) {
        Pageable pageable = PageRequest.of(0, top);
        return springDataRepository.topActive(from, to, pageable).stream()
                .map(row -> new SensorActivityDto((String) row[0], ((Number) row[1]).longValue()))
                .toList();
    }
}
