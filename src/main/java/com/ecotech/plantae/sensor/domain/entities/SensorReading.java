package com.ecotech.plantae.sensor.domain.entities;

import com.ecotech.plantae.sensor.domain.valueobjects.SensorId;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorMetric;

import java.time.Instant;
import java.util.Objects;

public class SensorReading {

    private final SensorId sensorId;
    private final Instant timestamp;
    private final SensorMetric metric;
    private final double value;
    private final String quality;

    public SensorReading(SensorId sensorId, Instant timestamp, SensorMetric metric, double value, String quality) {
        this.sensorId = Objects.requireNonNull(sensorId);
        this.timestamp = Objects.requireNonNull(timestamp);
        this.metric = Objects.requireNonNull(metric);
        this.value = value;
        this.quality = quality;
    }

    public SensorId getSensorId() {
        return sensorId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public SensorMetric getMetric() {
        return metric;
    }

    public double getValue() {
        return value;
    }

    public String getQuality() {
        return quality;
    }
}
