package com.ecotech.plantae.sensor.infrastructure.persistence.repository;

import com.ecotech.plantae.sensor.domain.entities.Sensor;
import com.ecotech.plantae.sensor.domain.models.PagedResult;
import com.ecotech.plantae.sensor.domain.models.SensorSearchCriteria;
import com.ecotech.plantae.sensor.domain.repositories.SensorRepository;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorId;
import com.ecotech.plantae.sensor.infrastructure.persistence.SensorJpaEntity;
import com.ecotech.plantae.sensor.infrastructure.persistence.mappers.SensorJpaMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SensorRepositoryJpa implements SensorRepository {

    private final SpringDataSensorRepository springDataSensorRepository;
    private final SensorJpaMapper mapper;

    public SensorRepositoryJpa(SpringDataSensorRepository springDataSensorRepository, SensorJpaMapper mapper) {
        this.springDataSensorRepository = springDataSensorRepository;
        this.mapper = mapper;
    }

    @Override
    public Sensor save(Sensor sensor) {
        SensorJpaEntity saved = springDataSensorRepository.save(mapper.toJpa(sensor));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Sensor> findById(SensorId id) {
        return springDataSensorRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public PagedResult<Sensor> search(SensorSearchCriteria criteria) {
        Specification<SensorJpaEntity> specification = SensorSpecifications.fromCriteria(criteria);
        Pageable pageable = PageRequest.of(Math.max(criteria.page(), 0), Math.max(criteria.size(), 1), Sort.by("createdAt").descending());
        Page<SensorJpaEntity> page = springDataSensorRepository.findAll(specification, pageable);
        return new PagedResult<>(
                page.map(mapper::toDomain).toList(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages()
        );
    }
}
