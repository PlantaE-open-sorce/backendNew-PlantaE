package com.ecotech.plantae.sensor.domain.entities;

import com.ecotech.plantae.plant.domain.valueobjects.PlantId;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorId;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorStatus;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorType;

import java.time.Instant;
import java.util.Objects;

public class Sensor {

    private final SensorId id;
    private final SensorType type;
    private SensorStatus status;
    private PlantId plantId;
    private String ownerId;
    private Instant lastReadingAt;
    private final Instant createdAt;
    private Instant updatedAt;

    private Sensor(SensorId id, SensorType type, SensorStatus status, PlantId plantId,
                   String ownerId, Instant lastReadingAt, Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id);
        this.type = Objects.requireNonNull(type);
        this.status = status == null ? SensorStatus.ACTIVE : status;
        this.plantId = plantId;
        this.ownerId = ownerId;
        this.lastReadingAt = lastReadingAt;
        this.createdAt = createdAt == null ? Instant.now() : createdAt;
        this.updatedAt = updatedAt == null ? this.createdAt : updatedAt;
    }

    public static Sensor register(SensorType type, String ownerId, PlantId plantId) {
        Objects.requireNonNull(ownerId, "Owner id is required");
        Objects.requireNonNull(plantId, "Plant id is required");
        return new Sensor(SensorId.newId(), type, SensorStatus.ACTIVE, plantId, ownerId, null, Instant.now(), Instant.now());
    }

    public static Sensor restore(SensorId id, SensorType type, SensorStatus status, PlantId plantId,
                                 String ownerId, Instant lastReadingAt, Instant createdAt, Instant updatedAt) {
        return new Sensor(id, type, status, plantId, ownerId, lastReadingAt, createdAt, updatedAt);
    }

    public void linkToPlant(PlantId plantId) {
        this.plantId = plantId;
        touch();
    }

    public void deactivate() {
        this.status = SensorStatus.INACTIVE;
        touch();
    }

    public void markReading(Instant timestamp) {
        this.lastReadingAt = timestamp;
        touch();
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    public SensorId getId() {
        return id;
    }

    public SensorType getType() {
        return type;
    }

    public SensorStatus getStatus() {
        return status;
    }

    public PlantId getPlantId() {
        return plantId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public Instant getLastReadingAt() {
        return lastReadingAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
