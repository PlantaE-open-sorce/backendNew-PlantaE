package com.ecotech.plantae.iam.application.handlers;

import com.ecotech.plantae.iam.application.queries.GetUserProfileQuery;
import com.ecotech.plantae.iam.domain.dtos.UserProfileDto;
import com.ecotech.plantae.iam.domain.repositories.UserRepository;

public class GetUserProfileHandler {

    private final UserRepository userRepository;

    public GetUserProfileHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileDto handle(GetUserProfileQuery query) {
        String normalizedEmail = query.email() == null ? null : query.email().trim().toLowerCase();
        return userRepository.findByEmail(normalizedEmail)
                .map(user -> new UserProfileDto(
                        user.getId().value(),
                        user.getEmail(),
                        user.getDisplayName(),
                        user.getAccountType().name()
                ))
                .orElse(null);
    }
}
