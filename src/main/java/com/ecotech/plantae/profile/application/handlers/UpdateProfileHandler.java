package com.ecotech.plantae.profile.application.handlers;

import com.ecotech.plantae.iam.domain.valueobjects.UserId;
import com.ecotech.plantae.profile.application.commands.UpdateProfileCommand;
import com.ecotech.plantae.profile.domain.dtos.ProfileDto;
import com.ecotech.plantae.profile.domain.entities.Profile;
import com.ecotech.plantae.profile.domain.repositories.ProfileRepository;
import com.ecotech.plantae.profile.domain.valueobjects.ProfileSlug;

public class UpdateProfileHandler {

    private final ProfileRepository profileRepository;

    public UpdateProfileHandler(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public ProfileDto handle(UpdateProfileCommand command) {
        Profile profile = profileRepository.findByOwnerId(UserId.of(command.ownerId()))
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));
        profile.updatePublicFields(command.displayName(), command.bio(), command.avatarUrl(), command.location(), command.timezone());
        if (command.slug() != null && !command.slug().isBlank()) {
            ProfileSlug slug = ProfileSlug.of(command.slug());
            if (!slug.value().equals(profile.getSlug().value()) && profileRepository.existsBySlug(slug)) {
                throw new IllegalArgumentException("Slug already in use");
            }
            profile.setSlug(slug);
        }
        return ProfileDtoMapper.toDto(profileRepository.save(profile));
    }
}
