package com.ecotech.plantae.profile.application.internal.commands;

import java.util.List;

public record UpdateNotificationPreferencesCommand(String ownerId,
        String quietHoursStart,
        String quietHoursEnd,
        String digestTime,
        List<NotificationPreferenceInput> preferences) {

    public record NotificationPreferenceInput(String type, boolean emailEnabled, boolean inAppEnabled) {
    }
}
