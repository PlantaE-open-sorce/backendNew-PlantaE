package com.ecotech.plantae.iam.application.internal.commandhandlers;

import com.ecotech.plantae.iam.application.internal.commands.ResetPasswordCommand;
import com.ecotech.plantae.iam.application.internal.outboundservices.PasswordHasher;
import com.ecotech.plantae.iam.domain.repositories.UserRepository;

import java.time.Instant;

public class ResetPasswordHandler {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public ResetPasswordHandler(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public void handle(ResetPasswordCommand command) {
        var user = userRepository.findByResetToken(command.token())
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));
        if (user.getPasswordResetTokenExpiresAt() == null || Instant.now().isAfter(user.getPasswordResetTokenExpiresAt())) {
            user.markPasswordReset(null, null);
            userRepository.save(user);
            throw new IllegalArgumentException("Invalid or expired token");
        }
        user.updatePassword(passwordHasher.hash(command.newPassword()));
        userRepository.save(user);
    }
}
