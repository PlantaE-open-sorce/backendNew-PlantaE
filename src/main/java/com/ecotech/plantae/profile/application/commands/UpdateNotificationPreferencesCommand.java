package com.ecotech.plantae.profile.application.commands;

import com.ecotech.plantae.profile.domain.dtos.NotificationPreferenceDto;

import java.util.List;

public record UpdateNotificationPreferencesCommand(String ownerId,
                                                   String quietHoursStart,
                                                   String quietHoursEnd,
                                                   String digestTime,
                                                   List<NotificationPreferenceDto> preferences) {}
