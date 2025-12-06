package com.ecotech.plantae.profile.application.internal.handlers;

import com.ecotech.plantae.iam.domain.valueobjects.UserId;
import com.ecotech.plantae.iam.domain.valueobjects.UserLanguage;
import com.ecotech.plantae.profile.application.internal.commands.UpdateProfileDetailsCommand;
import com.ecotech.plantae.profile.domain.repositories.ProfileRepository;

public class UpdateProfileDetailsHandler {

    private final ProfileRepository profileRepository;

    public UpdateProfileDetailsHandler(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public void handle(UpdateProfileDetailsCommand command) {
        var profile = profileRepository.findByOwnerId(UserId.of(command.ownerId()))
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));
        profile.updateProfileDetails(command.fullName(), command.timezone(), UserLanguage.from(command.language()));
        profileRepository.save(profile);
    }
}
