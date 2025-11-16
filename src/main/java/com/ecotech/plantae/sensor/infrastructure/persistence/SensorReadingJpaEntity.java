package com.ecotech.plantae.sensor.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "sensor_readings", indexes = {
        @Index(name = "idx_sensor_time", columnList = "sensorId,timestamp")
})
public class SensorReadingJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String sensorId;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false)
    private String metric;

    @Column(nullable = false)
    private double value;

    private String quality;

    protected SensorReadingJpaEntity() {
    }

    public SensorReadingJpaEntity(String sensorId, Instant timestamp, String metric, double value, String quality) {
        this.sensorId = sensorId;
        this.timestamp = timestamp;
        this.metric = metric;
        this.value = value;
        this.quality = quality;
    }

    public String getSensorId() {
        return sensorId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getMetric() {
        return metric;
    }

    public double getValue() {
        return value;
    }

    public String getQuality() {
        return quality;
    }
}
