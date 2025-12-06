package com.ecotech.plantae.profile.application.internal.handlers;

import com.ecotech.plantae.iam.domain.valueobjects.UserId;
import com.ecotech.plantae.profile.domain.entities.Profile;
import com.ecotech.plantae.profile.domain.repositories.ProfileRepository;

public class GetNotificationPreferencesHandler {

    private final ProfileRepository profileRepository;

    public GetNotificationPreferencesHandler(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Profile handle(String ownerId) {
        return profileRepository.findByOwnerId(UserId.of(ownerId))
                .orElse(null);
    }
}
