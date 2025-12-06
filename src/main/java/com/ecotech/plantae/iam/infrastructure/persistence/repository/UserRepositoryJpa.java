package com.ecotech.plantae.iam.infrastructure.persistence.repository;

import com.ecotech.plantae.iam.domain.entities.User;
import com.ecotech.plantae.iam.domain.repositories.UserRepository;
import com.ecotech.plantae.iam.domain.valueobjects.UserId;
import com.ecotech.plantae.iam.infrastructure.persistence.mappers.UserJpaMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@SuppressWarnings("null")
public class UserRepositoryJpa implements UserRepository {

    private final SpringDataUserRepository springDataRepository;
    private final UserJpaMapper mapper;

    public UserRepositoryJpa(SpringDataUserRepository springDataRepository, UserJpaMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        return mapper.toDomain(springDataRepository.save(mapper.toJpa(user)));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springDataRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findById(UserId id) {
        return springDataRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByResetToken(String token) {
        return springDataRepository.findByPasswordResetToken(token).map(mapper::toDomain);
    }

    @Override
    public void delete(UserId userId) {
        springDataRepository.deleteById(userId.value());
    }
}
