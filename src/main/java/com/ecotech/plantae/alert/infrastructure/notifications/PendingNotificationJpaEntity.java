package com.ecotech.plantae.alert.infrastructure.notifications;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "pending_notifications")
@SuppressWarnings("unused")
public class PendingNotificationJpaEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String ownerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Channel channel;

    @Column(nullable = false)
    private String payload;

    @Column(nullable = false)
    private Instant scheduledAt;

    private Instant sentAt;

    protected PendingNotificationJpaEntity() {
    }

    public PendingNotificationJpaEntity(String ownerId, Channel channel, String payload, Instant scheduledAt) {
        this.id = UUID.randomUUID().toString();
        this.ownerId = ownerId;
        this.channel = channel;
        this.payload = payload;
        this.scheduledAt = scheduledAt;
    }

    public enum Channel {
        EMAIL,
        IN_APP
    }
}
