package com.ecotech.plantae.sensor.domain.models;

import com.ecotech.plantae.sensor.domain.valueobjects.SensorMetric;

import java.time.Instant;

public record SensorReadingSearchCriteria(
        String sensorId,
        Instant from,
        Instant to,
        SensorMetric metric,
        int page,
        int size
) {}
