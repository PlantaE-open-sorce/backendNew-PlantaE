package com.ecotech.plantae.iam.application.handlers;

import com.ecotech.plantae.iam.application.commands.RegisterUserCommand;
import com.ecotech.plantae.iam.application.ports.PasswordHasher;
import com.ecotech.plantae.iam.domain.entities.User;
import com.ecotech.plantae.iam.domain.valueobjects.UserAccountType;
import com.ecotech.plantae.iam.domain.repositories.UserRepository;
import com.ecotech.plantae.iam.domain.valueobjects.UserLanguage;

public class RegisterUserHandler {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public RegisterUserHandler(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public User handle(RegisterUserCommand command) {
        String normalizedEmail = normalizeEmail(command.email());
        userRepository.findByEmail(normalizedEmail).ifPresent(user -> {
            throw new IllegalStateException("Email already registered");
        });
        UserLanguage language = UserLanguage.from(command.language());
        UserAccountType accountType = UserAccountType.from(command.accountType());
        User user = User.register(
                normalizedEmail,
                command.password(),
                command.displayName(),
                language,
                accountType,
                passwordHasher::hash
        );
        return userRepository.save(user);
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
