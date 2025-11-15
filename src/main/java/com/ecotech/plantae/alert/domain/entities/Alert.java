package com.ecotech.plantae.alert.domain.entities;

import com.ecotech.plantae.alert.domain.valueobjects.AlertId;
import com.ecotech.plantae.alert.domain.valueobjects.AlertStatus;
import com.ecotech.plantae.alert.domain.valueobjects.AlertType;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorId;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Alert {

    private final AlertId id;
    private final PlantId plantId;
    private final SensorId sensorId;
    private final String ownerId;
    private final AlertType type;
    private AlertStatus status;
    private final Instant occurredAt;
    private Instant resolvedAt;
    private final Map<String, Object> metadata = new HashMap<>();

    private Alert(AlertId id, PlantId plantId, SensorId sensorId, String ownerId, AlertType type,
                  AlertStatus status, Instant occurredAt, Instant resolvedAt, Map<String, Object> metadata) {
        this.id = Objects.requireNonNull(id);
        this.plantId = Objects.requireNonNull(plantId);
        this.sensorId = sensorId;
        this.ownerId = Objects.requireNonNull(ownerId);
        this.type = Objects.requireNonNull(type);
        this.status = status == null ? AlertStatus.ACTIVE : status;
        this.occurredAt = occurredAt == null ? Instant.now() : occurredAt;
        this.resolvedAt = resolvedAt;
        if (metadata != null) {
            this.metadata.putAll(metadata);
        }
    }

    public static Alert raise(PlantId plantId, SensorId sensorId, String ownerId, AlertType type) {
        return new Alert(AlertId.newId(), plantId, sensorId, ownerId, type, AlertStatus.ACTIVE, Instant.now(), null, null);
    }

    public static Alert restore(AlertId id, PlantId plantId, SensorId sensorId, String ownerId,
                                AlertType type, AlertStatus status, Instant occurredAt,
                                Instant resolvedAt, Map<String, Object> metadata) {
        return new Alert(id, plantId, sensorId, ownerId, type, status, occurredAt, resolvedAt, metadata);
    }

    public void resolve() {
        this.status = AlertStatus.RECOVERED;
        this.resolvedAt = Instant.now();
    }

    public void addMetadata(String key, Object value) {
        metadata.put(key, value);
    }

    public AlertId getId() {
        return id;
    }

    public PlantId getPlantId() {
        return plantId;
    }

    public SensorId getSensorId() {
        return sensorId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public AlertType getType() {
        return type;
    }

    public AlertStatus getStatus() {
        return status;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public Instant getResolvedAt() {
        return resolvedAt;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }
}
