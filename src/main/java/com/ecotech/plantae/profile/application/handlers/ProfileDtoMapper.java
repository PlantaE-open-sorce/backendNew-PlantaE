package com.ecotech.plantae.profile.application.handlers;

import com.ecotech.plantae.profile.domain.dtos.ProfileDto;
import com.ecotech.plantae.profile.domain.entities.Profile;

final class ProfileDtoMapper {

    private ProfileDtoMapper() {
    }

    static ProfileDto toDto(Profile profile) {
        return new ProfileDto(
                profile.getId().value(),
                profile.getOwnerId().value(),
                profile.getDisplayName(),
                profile.getBio(),
                profile.getAvatarUrl(),
                profile.getLocation(),
                profile.getTimezone(),
                profile.getSlug().value()
        );
    }
}
