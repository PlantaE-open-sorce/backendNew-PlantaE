package com.ecotech.plantae.profile.application.internal.handlers;

import com.ecotech.plantae.profile.application.internal.queries.GetProfileBySlugQuery;
import com.ecotech.plantae.profile.domain.entities.Profile;
import com.ecotech.plantae.profile.domain.repositories.ProfileRepository;
import com.ecotech.plantae.profile.domain.valueobjects.ProfileSlug;

public class GetProfileBySlugHandler {

    private final ProfileRepository profileRepository;

    public GetProfileBySlugHandler(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Profile handle(GetProfileBySlugQuery query) {
        ProfileSlug slug = ProfileSlug.of(query.slug());
        return profileRepository.findBySlug(slug)
                .orElse(null);
    }
}
