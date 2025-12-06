package com.ecotech.plantae.iam.application.internal.queryhandlers;

import com.ecotech.plantae.iam.application.internal.queries.GetCurrentUserProfileQuery;
import com.ecotech.plantae.iam.domain.repositories.UserRepository;
import com.ecotech.plantae.iam.domain.valueobjects.UserId;

import com.ecotech.plantae.iam.domain.entities.User;

public class GetCurrentUserProfileHandler {

    private final UserRepository userRepository;

    public GetCurrentUserProfileHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handle(GetCurrentUserProfileQuery query) {
        return userRepository.findById(UserId.of(query.userId()))
                .orElse(null);
    }
}
