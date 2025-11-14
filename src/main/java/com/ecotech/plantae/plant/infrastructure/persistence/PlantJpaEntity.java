package com.ecotech.plantae.plant.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "plants")
public class PlantJpaEntity {

    @Id
    private String id;

    @Column(nullable = false, length = 64)
    private String ownerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String species;

    private String deviceId;

    private String sensorId;

    @Column(nullable = false, length = 15)
    private String status;

    @Column(nullable = false)
    private boolean hasAlerts;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    private Instant deletedAt;

    protected PlantJpaEntity() {
    }

    public PlantJpaEntity(String id, String ownerId, String name, String species, String deviceId,
                          String sensorId, String status, boolean hasAlerts,
                          Instant createdAt, Instant updatedAt, Instant deletedAt) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.species = species;
        this.deviceId = deviceId;
        this.sensorId = sensorId;
        this.status = status;
        this.hasAlerts = hasAlerts;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public String getId() {
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

    public String getDeviceId() {
        return deviceId;
    }

    public String getSensorId() {
        return sensorId;
    }

    public String getStatus() {
        return status;
    }

    public boolean isHasAlerts() {
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
}
