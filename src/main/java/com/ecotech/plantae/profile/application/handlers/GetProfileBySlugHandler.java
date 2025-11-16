package com.ecotech.plantae.profile.application.handlers;

import com.ecotech.plantae.profile.application.queries.GetProfileBySlugQuery;
import com.ecotech.plantae.profile.domain.dtos.ProfileDto;
import com.ecotech.plantae.profile.domain.repositories.ProfileRepository;
import com.ecotech.plantae.profile.domain.valueobjects.ProfileSlug;

public class GetProfileBySlugHandler {

    private final ProfileRepository profileRepository;

    public GetProfileBySlugHandler(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public ProfileDto handle(GetProfileBySlugQuery query) {
        ProfileSlug slug = ProfileSlug.of(query.slug());
        return profileRepository.findBySlug(slug)
                .map(ProfileDtoMapper::toDto)
                .orElse(null);
    }
}
