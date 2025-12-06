package com.ecotech.plantae.iam.domain.repositories;

import com.ecotech.plantae.iam.domain.entities.User;
import com.ecotech.plantae.iam.domain.valueobjects.UserId;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UserId id);
    Optional<User> findByResetToken(String token);
    void delete(UserId userId);
}
