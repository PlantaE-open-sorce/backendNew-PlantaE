package com.ecotech.plantae.profile.application.handlers;

import com.ecotech.plantae.iam.domain.valueobjects.UserId;
import com.ecotech.plantae.profile.application.commands.CreateProfileCommand;
import com.ecotech.plantae.profile.application.services.SlugService;
import com.ecotech.plantae.profile.domain.dtos.ProfileDto;
import com.ecotech.plantae.profile.domain.entities.Profile;
import com.ecotech.plantae.profile.domain.repositories.ProfileRepository;
import com.ecotech.plantae.profile.domain.valueobjects.ProfileSlug;

public class CreateProfileHandler {

    private final ProfileRepository profileRepository;
    private final SlugService slugService;

    public CreateProfileHandler(ProfileRepository profileRepository, SlugService slugService) {
        this.profileRepository = profileRepository;
        this.slugService = slugService;
    }

    public ProfileDto handle(CreateProfileCommand command) {
        UserId ownerId = UserId.of(command.ownerId());
        profileRepository.findByOwnerId(ownerId).ifPresent(existing -> {
            throw new IllegalStateException("Profile already exists for owner");
        });
        ProfileSlug slug = ensureUniqueSlug(command.displayName());
        Profile profile = Profile.create(ownerId, command.displayName(), ignored -> slug);
        return ProfileDtoMapper.toDto(profileRepository.save(profile));
    }

    private ProfileSlug ensureUniqueSlug(String displayName) {
        String base = slugService.toSlug(displayName);
        String candidate = base;
        int counter = 2;
        ProfileSlug slug = ProfileSlug.of(candidate);
        while (profileRepository.existsBySlug(slug)) {
            candidate = base + "-" + counter++;
            slug = ProfileSlug.of(candidate);
        }
        return slug;
    }

}
