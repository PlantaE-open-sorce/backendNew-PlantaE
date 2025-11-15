package com.ecotech.plantae.device.domain.entities;

import com.ecotech.plantae.device.domain.valueobjects.DeviceId;
import com.ecotech.plantae.device.domain.valueobjects.OwnerId;

import java.util.Objects;

public class Device {

    private final DeviceId id;
    private OwnerId ownerId;
    private final String hwModel;
    private final String secret;
    private boolean active;
    private String note;

    private Device(DeviceId id, OwnerId ownerId, String hwModel, String secret, boolean active, String note) {
        this.id = Objects.requireNonNull(id);
        this.ownerId = Objects.requireNonNull(ownerId);
        this.hwModel = Objects.requireNonNull(hwModel);
        this.secret = secret;
        this.active = active;
        this.note = note;
    }

    public static Device register(DeviceId id, OwnerId ownerId, String hwModel, String secret) {
        return new Device(id, ownerId, hwModel, secret, true, null);
    }

    public static Device restore(DeviceId id, OwnerId ownerId, String hwModel, String secret, boolean active, String note) {
        return new Device(id, ownerId, hwModel, secret, active, note);
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void transferTo(OwnerId newOwner) {
        this.ownerId = Objects.requireNonNull(newOwner);
    }

    public void updateNote(String note) {
        this.note = note;
    }

    public DeviceId getId() {
        return id;
    }

    public OwnerId getOwnerId() {
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
