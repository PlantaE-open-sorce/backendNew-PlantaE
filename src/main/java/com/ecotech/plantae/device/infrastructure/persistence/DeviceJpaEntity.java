package com.ecotech.plantae.device.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "devices")
public class DeviceJpaEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String ownerId;

    @Column(nullable = false)
    private String hwModel;

    private String secret;

    @Column(nullable = false)
    private boolean active;

    private String note;

    protected DeviceJpaEntity() {
    }

    public DeviceJpaEntity(String id, String ownerId, String hwModel, String secret, boolean active, String note) {
        this.id = id;
        this.ownerId = ownerId;
        this.hwModel = hwModel;
        this.secret = secret;
        this.active = active;
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getHwModel() {
        return hwModel;
    }

    public String getSecret() {
        return secret;
    }

    public boolean isActive() {
        return active;
    }

    public String getNote() {
        return note;
    }
}
