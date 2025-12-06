package com.ecotech.plantae.iam.application.internal.queryhandlers;

import com.ecotech.plantae.iam.application.internal.queries.GetUserProfileQuery;
import com.ecotech.plantae.iam.domain.entities.User;
import com.ecotech.plantae.iam.domain.repositories.UserRepository;

public class GetUserProfileHandler {

    private final UserRepository userRepository;

    public GetUserProfileHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handle(GetUserProfileQuery query) {
        String normalizedEmail = query.email() == null ? null : query.email().trim().toLowerCase();
        return userRepository.findByEmail(normalizedEmail)
                .orElse(null);
    }
}
