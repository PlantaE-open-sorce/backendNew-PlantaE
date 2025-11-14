package com.ecotech.plantae.iam.application.handlers;

import com.ecotech.plantae.iam.application.commands.LoginUserCommand;
import com.ecotech.plantae.iam.application.ports.PasswordHasher;
import com.ecotech.plantae.iam.domain.entities.User;
import com.ecotech.plantae.iam.domain.repositories.UserRepository;

public class LoginUserHandler {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public LoginUserHandler(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public User handle(LoginUserCommand command) {
        String normalizedEmail = command.email() == null ? null : command.email().trim().toLowerCase();
        return userRepository.findByEmail(normalizedEmail)
                .filter(User::isActive)
                .filter(user -> passwordHasher.matches(command.password(), user.getPasswordHash()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
    }
}
