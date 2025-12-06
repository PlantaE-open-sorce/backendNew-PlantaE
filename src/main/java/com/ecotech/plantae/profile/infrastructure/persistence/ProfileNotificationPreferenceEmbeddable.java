package com.ecotech.plantae.profile.infrastructure.persistence;

import com.ecotech.plantae.profile.domain.valueobjects.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class ProfileNotificationPreferenceEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private NotificationType type;

    @Column(name = "email_enabled")
    private boolean emailEnabled;

    @Column(name = "in_app_enabled")
    private boolean inAppEnabled;

    protected ProfileNotificationPreferenceEmbeddable() {
    }

    public ProfileNotificationPreferenceEmbeddable(NotificationType type, boolean emailEnabled, boolean inAppEnabled) {
        this.type = type;
        this.emailEnabled = emailEnabled;
        this.inAppEnabled = inAppEnabled;
    }

    public NotificationType getType() {
        return type;
    }

    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    public boolean isInAppEnabled() {
        return inAppEnabled;
    }
}
