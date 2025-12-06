package com.ecotech.plantae.iam.application.internal.commandhandlers;

import com.ecotech.plantae.iam.application.internal.commands.ChangePasswordCommand;
import com.ecotech.plantae.iam.application.internal.outboundservices.PasswordHasher;
import com.ecotech.plantae.iam.domain.repositories.UserRepository;
import com.ecotech.plantae.iam.domain.valueobjects.UserId;

public class ChangePasswordHandler {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public ChangePasswordHandler(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public void handle(ChangePasswordCommand command) {
        var user = userRepository.findById(UserId.of(command.userId()))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!passwordHasher.matches(command.currentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        user.updatePassword(passwordHasher.hash(command.newPassword()));
        userRepository.save(user);
    }
}
