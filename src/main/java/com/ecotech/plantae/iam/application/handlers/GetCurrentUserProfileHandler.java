package com.ecotech.plantae.iam.application.handlers;

import com.ecotech.plantae.iam.application.queries.GetCurrentUserProfileQuery;
import com.ecotech.plantae.iam.domain.dtos.UserProfileDto;
import com.ecotech.plantae.iam.domain.repositories.UserRepository;
import com.ecotech.plantae.iam.domain.valueobjects.UserId;

public class GetCurrentUserProfileHandler {

    private final UserRepository userRepository;

    public GetCurrentUserProfileHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileDto handle(GetCurrentUserProfileQuery query) {
        return userRepository.findById(UserId.of(query.userId()))
                .map(user -> new UserProfileDto(
                        user.getId().value(),
                        user.getEmail(),
                        user.getDisplayName(),
                        user.getAccountType().name()
                ))
                .orElse(null);
    }
}
