package com.ecotech.plantae.profile.domain.entities;

import com.ecotech.plantae.profile.domain.valueobjects.NotificationChannel;
import com.ecotech.plantae.profile.domain.valueobjects.NotificationType;

public class NotificationPreference {

    private final NotificationType type;
    private boolean emailEnabled;
    private boolean inAppEnabled;

    public NotificationPreference(NotificationType type, boolean emailEnabled, boolean inAppEnabled) {
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

    public void setChannel(NotificationChannel channel, boolean enabled) {
        if (channel == NotificationChannel.EMAIL) {
            this.emailEnabled = enabled;
        } else if (channel == NotificationChannel.IN_APP) {
            this.inAppEnabled = enabled;
        }
    }
}
