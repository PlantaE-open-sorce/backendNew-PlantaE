package com.ecotech.plantae.alert.infrastructure.persistence.repository;

import com.ecotech.plantae.alert.domain.entities.Alert;
import com.ecotech.plantae.alert.domain.models.AlertSearchCriteria;
import com.ecotech.plantae.alert.domain.repositories.AlertRepository;
import com.ecotech.plantae.alert.domain.valueobjects.AlertId;
import com.ecotech.plantae.alert.domain.valueobjects.AlertStatus;
import com.ecotech.plantae.alert.infrastructure.persistence.mappers.AlertJpaMapper;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AlertRepositoryJpa implements AlertRepository {

    private final SpringDataAlertRepository springDataAlertRepository;
    private final AlertJpaMapper mapper;

    public AlertRepositoryJpa(SpringDataAlertRepository springDataAlertRepository, AlertJpaMapper mapper) {
        this.springDataAlertRepository = springDataAlertRepository;
        this.mapper = mapper;
    }

    @Override
    public Alert save(Alert alert) {
        return mapper.toDomain(springDataAlertRepository.save(mapper.toJpa(alert)));
    }

    @Override
    public Optional<Alert> findById(AlertId id) {
        return springDataAlertRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public List<Alert> findRecent(String ownerId, AlertSearchCriteria criteria) {
        var pageRequest = PageRequest.of(Math.max(criteria.page(), 0), Math.max(criteria.size(), 1));
        String type = criteria.type() != null ? criteria.type().name() : null;
        return springDataAlertRepository.findRecent(ownerId, criteria.plantId(), criteria.sensorId(), type, pageRequest)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Alert> findByPlant(PlantId plantId, int page, int size) {
        return springDataAlertRepository.findByPlantIdOrderByOccurredAtDesc(plantId.value(), PageRequest.of(Math.max(page, 0), Math.max(size, 1)))
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public long countActiveByPlant(PlantId plantId) {
        return springDataAlertRepository.countByPlantIdAndStatus(plantId.value(), AlertStatus.ACTIVE.name());
    }
}
