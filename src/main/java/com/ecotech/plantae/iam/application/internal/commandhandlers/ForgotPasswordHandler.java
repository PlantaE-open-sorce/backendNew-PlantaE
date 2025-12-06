package com.ecotech.plantae.iam.application.internal.commandhandlers;

import com.ecotech.plantae.iam.application.internal.commands.ForgotPasswordCommand;
import com.ecotech.plantae.iam.application.internal.outboundservices.PasswordResetNotifier;
import com.ecotech.plantae.iam.domain.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.UUID;

public class ForgotPasswordHandler {

    private static final Logger log = LoggerFactory.getLogger(ForgotPasswordHandler.class);

    private final UserRepository userRepository;
    private final PasswordResetNotifier passwordResetNotifier;

    public ForgotPasswordHandler(UserRepository userRepository, PasswordResetNotifier passwordResetNotifier) {
        this.userRepository = userRepository;
        this.passwordResetNotifier = passwordResetNotifier;
    }

    public void handle(ForgotPasswordCommand command) {
        String normalizedEmail = normalizeEmail(command.email());
        log.info("Processing forgot password request for email: {}", normalizedEmail);
        Locale locale = command.locale() == null ? Locale.ENGLISH : command.locale();
        var userOpt = userRepository.findByEmail(normalizedEmail);
        if (userOpt.isEmpty()) {
            log.warn("No user found with email: {}", normalizedEmail);
            return;
        }
        userOpt.ifPresent(user -> {
            log.info("User found: {}, generating reset token", user.getEmail());
            user.markPasswordReset(UUID.randomUUID().toString(), Instant.now().plus(15, ChronoUnit.MINUTES));
            userRepository.save(user);
            log.info("Reset token saved for user: {}", user.getEmail());
            Locale userLocale = Locale.forLanguageTag(user.getLanguage().name().toLowerCase());
            passwordResetNotifier.send(user.getEmail(), user.getPasswordResetToken(),
                    userLocale != null ? userLocale : locale);
        });
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
