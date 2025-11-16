package com.ecotech.plantae.profile.domain.dtos;

import java.util.List;

public record NotificationSettingsDto(
        String quietHoursStart,
        String quietHoursEnd,
        String digestTime,
        List<NotificationPreferenceDto> preferences
) {}
