package com.ecotech.plantae.profile.infrastructure.persistence.repository;

import com.ecotech.plantae.profile.infrastructure.persistence.ProfileJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataProfileRepository extends JpaRepository<ProfileJpaEntity, String> {

    Optional<ProfileJpaEntity> findBySlug(String slug);

    Optional<ProfileJpaEntity> findByOwnerId(String ownerId);

    boolean existsBySlug(String slug);
}
