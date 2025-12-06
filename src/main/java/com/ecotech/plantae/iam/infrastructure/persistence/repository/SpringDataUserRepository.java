package com.ecotech.plantae.iam.infrastructure.persistence.repository;

import com.ecotech.plantae.iam.infrastructure.persistence.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, String> {
    Optional<UserJpaEntity> findByEmail(String email);
    Optional<UserJpaEntity> findByPasswordResetToken(String token);
}
