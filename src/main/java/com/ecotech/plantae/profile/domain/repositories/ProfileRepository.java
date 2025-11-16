package com.ecotech.plantae.profile.domain.repositories;

import com.ecotech.plantae.iam.domain.valueobjects.UserId;
import com.ecotech.plantae.profile.domain.entities.Profile;
import com.ecotech.plantae.profile.domain.valueobjects.ProfileSlug;

import java.util.Optional;

public interface ProfileRepository {

    Profile save(Profile profile);

    Optional<Profile> findBySlug(ProfileSlug slug);

    Optional<Profile> findByOwnerId(UserId ownerId);

    boolean existsBySlug(ProfileSlug slug);
}
