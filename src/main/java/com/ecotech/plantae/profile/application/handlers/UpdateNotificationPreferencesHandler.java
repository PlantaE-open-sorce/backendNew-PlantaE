package com.ecotech.plantae.profile.application.handlers;

import com.ecotech.plantae.iam.domain.valueobjects.UserId;
import com.ecotech.plantae.profile.application.commands.UpdateNotificationPreferencesCommand;
import com.ecotech.plantae.profile.domain.entities.NotificationPreference;
import com.ecotech.plantae.profile.domain.repositories.ProfileRepository;
import com.ecotech.plantae.profile.domain.valueobjects.NotificationType;

import java.util.List;

public class UpdateNotificationPreferencesHandler {

    private final ProfileRepository profileRepository;

    public UpdateNotificationPreferencesHandler(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public void handle(UpdateNotificationPreferencesCommand command) {
        var profile = profileRepository.findByOwnerId(UserId.of(command.ownerId()))
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));
        List<NotificationPreference> preferences = command.preferences() == null ? List.of() :
                command.preferences().stream()
                        .map(dto -> new NotificationPreference(
                                NotificationType.valueOf(dto.type()),
                                dto.emailEnabled(),
                                dto.inAppEnabled()
                        ))
                        .toList();
        profile.updateNotificationPreferences(
                command.quietHoursStart(),
                command.quietHoursEnd(),
                command.digestTime(),
                preferences
        );
        profileRepository.save(profile);
    }
}
