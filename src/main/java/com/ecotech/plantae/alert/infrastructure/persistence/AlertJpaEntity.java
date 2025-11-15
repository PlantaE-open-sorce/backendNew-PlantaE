package com.ecotech.plantae.alert.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "alerts")
public class AlertJpaEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String plantId;

    private String sensorId;

    @Column(nullable = false)
    private String ownerId;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Instant occurredAt;

    private Instant resolvedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "meta_key")
    @Column(name = "meta_value")
    private Map<String, String> metadata = new HashMap<>();

    protected AlertJpaEntity() {
    }

    public AlertJpaEntity(String id, String plantId, String sensorId, String ownerId, String type,
                          String status, Instant occurredAt, Instant resolvedAt, Map<String, String> metadata) {
        this.id = id;
        this.plantId = plantId;
        this.sensorId = sensorId;
        this.ownerId = ownerId;
        this.type = type;
        this.status = status;
        this.occurredAt = occurredAt;
        this.resolvedAt = resolvedAt;
        if (metadata != null) {
            this.metadata = metadata;
        }
    }

    public String getId() {
        return id;
    }

    public String getPlantId() {
        return plantId;
    }

    public String getSensorId() {
        return sensorId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public Instant getResolvedAt() {
        return resolvedAt;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }
}
