package com.ecotech.plantae.sensor.domain.repositories;

import com.ecotech.plantae.sensor.domain.entities.Sensor;
import com.ecotech.plantae.sensor.domain.models.PagedResult;
import com.ecotech.plantae.sensor.domain.models.SensorSearchCriteria;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorId;

import java.util.Optional;

public interface SensorRepository {
    Sensor save(Sensor sensor);
    Optional<Sensor> findById(SensorId id);
    PagedResult<Sensor> search(SensorSearchCriteria criteria);
}
