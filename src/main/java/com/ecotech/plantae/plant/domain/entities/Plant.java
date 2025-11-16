package com.ecotech.plantae.plant.domain.entities;

import com.ecotech.plantae.plant.domain.valueobjects.DeviceId;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;
import com.ecotech.plantae.plant.domain.valueobjects.PlantStatus;

import java.time.Instant;
import java.util.Objects;

public class Plant {

    private final PlantId id;
    private final String ownerId;
    private String name;
    private String species;
    private DeviceId deviceId;
    private String sensorId;
    private PlantStatus status;
    private boolean hasAlerts;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Plant(PlantId id, String ownerId, String name, String species, DeviceId deviceId,
                  String sensorId, PlantStatus status, boolean hasAlerts,
                  Instant createdAt, Instant updatedAt, Instant deletedAt) {
        this.id = Objects.requireNonNull(id);
        this.ownerId = requireNonBlank(ownerId, "Plant owner is required");
        this.name = requireNonBlank(name, "Plant name is required");
        this.species = requireNonBlank(species, "Species is required");
        this.deviceId = deviceId;
        this.sensorId = normalize(sensorId);
        this.status = status == null ? PlantStatus.ACTIVE : status;
        this.hasAlerts = hasAlerts;
        this.createdAt = createdAt == null ? Instant.now() : createdAt;
        this.updatedAt = updatedAt == null ? this.createdAt : updatedAt;
        this.deletedAt = deletedAt;
    }

    public static Plant create(String ownerId, String name, String species, DeviceId deviceId, String sensorId) {
        return new Plant(PlantId.newId(), ownerId, name, species, deviceId, sensorId, PlantStatus.ACTIVE, false, Instant.now(), Instant.now(), null);
    }

    public static Plant restore(PlantId id, String ownerId, String name, String species, DeviceId deviceId,
                                String sensorId, PlantStatus status, boolean hasAlerts,
                                Instant createdAt, Instant updatedAt, Instant deletedAt) {
        return new Plant(id, ownerId, name, species, deviceId, sensorId, status, hasAlerts, createdAt, updatedAt, deletedAt);
    }

    public void assignDevice(DeviceId deviceId) {
        this.deviceId = deviceId;
        touch();
    }

    public void assignSensor(String sensorId) {
        this.sensorId = normalize(sensorId);
        touch();
    }

    public void updateDetails(String name, String species, String deviceId, String sensorId, PlantStatus status) {
        if (name != null) {
            this.name = requireNonBlank(name, "Plant name is required");
        }
        if (species != null) {
            this.species = requireNonBlank(species, "Species is required");
        }
        if (deviceId != null) {
            this.deviceId = DeviceId.of(deviceId);
        }
        if (sensorId != null) {
            this.sensorId = normalize(sensorId);
        }
        if (status != null) {
            this.status = status;
        }
        touch();
    }

    public void markDeleted() {
        this.deletedAt = Instant.now();
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    public void setHasAlerts(boolean hasAlerts) {
        this.hasAlerts = hasAlerts;
        touch();
    }

    public PlantId getId() {
        return id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public String getSensorId() {
        return sensorId;
    }

    public PlantStatus getStatus() {
        return status;
    }

    public boolean hasAlerts() {
        return hasAlerts;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    private String requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
