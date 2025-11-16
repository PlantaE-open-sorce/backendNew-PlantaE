package com.ecotech.plantae.sensor.application.handlers;

import com.ecotech.plantae.sensor.application.queries.GetSensorReadingsQuery;
import com.ecotech.plantae.sensor.domain.dtos.SensorReadingDto;
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

    public PagedResult<SensorReadingDto> handle(GetSensorReadingsQuery query) {
        SensorReadingSearchCriteria criteria = new SensorReadingSearchCriteria(
                query.sensorId(),
                parseInstant(query.from()),
                parseInstant(query.to()),
                query.metric() != null ? SensorMetric.from(query.metric()) : null,
                query.page(),
                query.size()
        );
        PagedResult<SensorReading> result = sensorReadingRepository.search(criteria);
        return new PagedResult<>(
                result.content().stream()
                        .map(reading -> new SensorReadingDto(
                                reading.getSensorId().value(),
                                reading.getTimestamp().toString(),
                                reading.getMetric().apiName(),
                                reading.getValue(),
                                reading.getQuality()
                        ))
                        .toList(),
                result.totalElements(),
                result.page(),
                result.size(),
                result.totalPages()
        );
    }

    private Instant parseInstant(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Instant.parse(value);
    }
}
