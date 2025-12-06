package com.ecotech.plantae.profile.infrastructure.persistence.repository;

import com.ecotech.plantae.iam.domain.valueobjects.UserId;
import com.ecotech.plantae.profile.domain.entities.Profile;
import com.ecotech.plantae.profile.domain.repositories.ProfileRepository;
import com.ecotech.plantae.profile.domain.valueobjects.ProfileSlug;
import com.ecotech.plantae.profile.infrastructure.persistence.mappers.ProfileJpaMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@SuppressWarnings("null")
public class ProfileRepositoryJpa implements ProfileRepository {

    private final SpringDataProfileRepository springDataRepository;
    private final ProfileJpaMapper mapper;

    public ProfileRepositoryJpa(SpringDataProfileRepository springDataRepository, ProfileJpaMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public Profile save(Profile profile) {
        return mapper.toDomain(springDataRepository.save(mapper.toJpa(profile)));
    }

    @Override
    public Optional<Profile> findBySlug(ProfileSlug slug) {
        return springDataRepository.findBySlug(slug.value()).map(mapper::toDomain);
    }

    @Override
    public Optional<Profile> findByOwnerId(UserId ownerId) {
        return springDataRepository.findByOwnerId(ownerId.value()).map(mapper::toDomain);
    }

    @Override
    public boolean existsBySlug(ProfileSlug slug) {
        return springDataRepository.existsBySlug(slug.value());
    }
}
