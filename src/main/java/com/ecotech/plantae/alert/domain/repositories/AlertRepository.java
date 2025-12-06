package com.ecotech.plantae.alert.domain.repositories;

import com.ecotech.plantae.alert.domain.entities.Alert;
import com.ecotech.plantae.alert.domain.models.AlertSearchCriteria;
import com.ecotech.plantae.alert.domain.valueobjects.AlertId;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;

import java.util.List;
import java.util.Optional;

public interface AlertRepository {
    Alert save(Alert alert);
    Optional<Alert> findById(AlertId id);
    List<Alert> findRecent(String ownerId, AlertSearchCriteria criteria);
    List<Alert> findByPlant(PlantId plantId, int page, int size);
    long countActiveByPlant(PlantId plantId);
}
