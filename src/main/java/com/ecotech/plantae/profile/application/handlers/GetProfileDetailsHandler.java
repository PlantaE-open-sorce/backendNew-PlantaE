package com.ecotech.plantae.profile.application.handlers;

import com.ecotech.plantae.iam.domain.valueobjects.UserId;
import com.ecotech.plantae.profile.domain.dtos.ProfileDetailsDto;
import com.ecotech.plantae.profile.domain.repositories.ProfileRepository;

public class GetProfileDetailsHandler {

    private final ProfileRepository profileRepository;

    public GetProfileDetailsHandler(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public ProfileDetailsDto handle(String ownerId) {
        return profileRepository.findByOwnerId(UserId.of(ownerId))
                .map(profile -> new ProfileDetailsDto(
                        profile.getFullName(),
                        profile.getTimezone(),
                        profile.getLanguage().name().toLowerCase(),
                        profile.getDisplayName(),
                        profile.getBio(),
                        profile.getAvatarUrl(),
                        profile.getLocation(),
                        profile.getSlug().value()
                ))
                .orElse(null);
    }
}
