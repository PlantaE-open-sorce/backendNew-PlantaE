package com.ecotech.plantae.profile.application.handlers;

import com.ecotech.plantae.iam.domain.valueobjects.UserId;
import com.ecotech.plantae.profile.domain.dtos.NotificationPreferenceDto;
import com.ecotech.plantae.profile.domain.dtos.NotificationSettingsDto;
import com.ecotech.plantae.profile.domain.repositories.ProfileRepository;

import java.util.List;

public class GetNotificationPreferencesHandler {

    private final ProfileRepository profileRepository;

    public GetNotificationPreferencesHandler(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public NotificationSettingsDto handle(String ownerId) {
        return profileRepository.findByOwnerId(UserId.of(ownerId))
                .map(profile -> new NotificationSettingsDto(
                        profile.getQuietHoursStart(),
                        profile.getQuietHoursEnd(),
                        profile.getDigestTime(),
                        profile.getNotificationPreferences().stream()
                                .map(pref -> new NotificationPreferenceDto(pref.getType().name(), pref.isEmailEnabled(), pref.isInAppEnabled()))
                                .toList()
                ))
                .orElse(null);
    }
}
