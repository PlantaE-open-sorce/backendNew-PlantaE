package com.ecotech.plantae.sensor.domain.repositories;

import com.ecotech.plantae.sensor.domain.dtos.SensorActivityDto;
import com.ecotech.plantae.sensor.domain.entities.SensorReading;
import com.ecotech.plantae.sensor.domain.models.PagedResult;
import com.ecotech.plantae.sensor.domain.models.SensorReadingSearchCriteria;

import java.time.Instant;
import java.util.List;

public interface SensorReadingRepository {
    void save(SensorReading reading);
    PagedResult<SensorReading> search(SensorReadingSearchCriteria criteria);
    List<SensorActivityDto> mostActive(Instant from, Instant to, int top);
}
