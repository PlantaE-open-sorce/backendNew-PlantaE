package com.ecotech.plantae.sensor.application.internal.handlers;

import com.ecotech.plantae.sensor.application.internal.queries.GetSensorReadingsQuery;
import com.ecotech.plantae.sensor.domain.entities.SensorReading;
import com.ecotech.plantae.sensor.domain.models.SensorReadingSearchCriteria;
import com.ecotech.plantae.sensor.domain.repositories.SensorReadingRepository;
import com.ecotech.plantae.sensor.domain.models.PagedResult;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorMetric;

import java.time.Instant;

public class GetSensorReadingsHandler {

    private final SensorReadingRepository sensorReadingRepository;

    public GetSensorReadingsHandler(SensorReadingRepository sensorReadingRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
    }

    public PagedResult<SensorReading> handle(GetSensorReadingsQuery query) {
        SensorReadingSearchCriteria criteria = new SensorReadingSearchCriteria(
                query.sensorId(),
                parseInstant(query.from()),
                parseInstant(query.to()),
                query.metric() != null
                        ? SensorMetric.from(query.metric())
                        : null,
                query.page(),
                query.size());
        return sensorReadingRepository.search(criteria);
    }

    private Instant parseInstant(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Instant.parse(value);
    }
}
