package com.ecotech.plantae.sensor.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "sensors")
public class SensorJpaEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String status;

    private String plantId;

    @Column(nullable = false)
    private String ownerId;

    private Instant lastReadingAt;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected SensorJpaEntity() {
    }

    public SensorJpaEntity(String id, String type, String status, String plantId, String ownerId,
                           Instant lastReadingAt, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.plantId = plantId;
        this.ownerId = ownerId;
        this.lastReadingAt = lastReadingAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getPlantId() {
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
