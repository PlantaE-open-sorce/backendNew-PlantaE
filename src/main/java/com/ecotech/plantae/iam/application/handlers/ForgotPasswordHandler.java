package com.ecotech.plantae.iam.application.handlers;

import com.ecotech.plantae.iam.application.commands.ForgotPasswordCommand;
import com.ecotech.plantae.iam.domain.repositories.UserRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class ForgotPasswordHandler {

    private final UserRepository userRepository;

    public ForgotPasswordHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void handle(ForgotPasswordCommand command) {
        userRepository.findByEmail(command.email())
                .ifPresent(user -> {
                    user.markPasswordReset(UUID.randomUUID().toString(), Instant.now().plus(1, ChronoUnit.HOURS));
                    userRepository.save(user);
                });
    }
}
