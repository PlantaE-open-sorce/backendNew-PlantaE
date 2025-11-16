package com.ecotech.plantae.iam.domain.entities;

import com.ecotech.plantae.iam.domain.valueobjects.UserAccountType;
import com.ecotech.plantae.iam.domain.valueobjects.UserId;
import com.ecotech.plantae.iam.domain.valueobjects.UserLanguage;
import com.ecotech.plantae.iam.domain.valueobjects.UserRole;

import java.time.Instant;
import java.util.Objects;
import java.util.function.Function;

public class User {

    private final UserId id;
    private final String email;
    private String passwordHash;
    private String displayName;
    private UserLanguage language;
    private UserRole role;
    private final UserAccountType accountType;
    private boolean active;
    private String passwordResetToken;
    private Instant passwordResetTokenExpiresAt;

    private User(UserId id,
                 String email,
                 String passwordHash,
                 String displayName,
                 UserLanguage language,
                 UserRole role,
                 UserAccountType accountType,
                 boolean active,
                 String passwordResetToken,
                 Instant passwordResetTokenExpiresAt) {
        this.id = Objects.requireNonNull(id, "User id is required");
        this.email = Objects.requireNonNull(email, "Email is required");
        this.passwordHash = Objects.requireNonNull(passwordHash, "Password hash is required");
        this.displayName = Objects.requireNonNull(displayName, "Display name is required");
        this.language = Objects.requireNonNull(language, "Language is required");
        this.role = Objects.requireNonNull(role, "Role is required");
        this.accountType = Objects.requireNonNull(accountType, "Account type is required");
        this.active = active;
        this.passwordResetToken = passwordResetToken;
        this.passwordResetTokenExpiresAt = passwordResetTokenExpiresAt;
    }

    public static User register(String email,
                                String rawPassword,
                                String displayName,
                                UserLanguage language,
                                UserAccountType accountType,
                                Function<String, String> hasher) {
        Objects.requireNonNull(hasher, "Password hasher is required");
        Objects.requireNonNull(email, "Email is required");
        Objects.requireNonNull(rawPassword, "Password is required");
        Objects.requireNonNull(displayName, "Display name is required");
        Objects.requireNonNull(accountType, "Account type is required");
        return new User(
                UserId.newId(),
                email.trim().toLowerCase(),
                hasher.apply(rawPassword),
                displayName.trim(),
                language,
                UserRole.USER,
                accountType,
                true,
                null,
                null
        );
    }

    public static User restore(UserId id,
                               String email,
                               String passwordHash,
                               String displayName,
                               UserLanguage language,
                               UserRole role,
                               UserAccountType accountType,
                               boolean active,
                               String passwordResetToken,
                               Instant passwordResetTokenExpiresAt) {
        return new User(id, email, passwordHash, displayName, language, role, accountType, active, passwordResetToken, passwordResetTokenExpiresAt);
    }

    public UserId getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getDisplayName() {
        return displayName;
    }

    public UserLanguage getLanguage() {
        return language;
    }

    public UserRole getRole() {
        return role;
    }

    public UserAccountType getAccountType() {
        return accountType;
    }

    public boolean isActive() {
        return active;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public Instant getPasswordResetTokenExpiresAt() {
        return passwordResetTokenExpiresAt;
    }

    public void updateDisplayName(String value) {
        this.displayName = Objects.requireNonNull(value, "Display name is required").trim();
    }

    public void updateLanguage(UserLanguage language) {
        this.language = Objects.requireNonNull(language, "Language is required");
    }

    public void updatePassword(String hashedPassword) {
        this.passwordHash = Objects.requireNonNull(hashedPassword, "Password hash is required");
        this.passwordResetToken = null;
        this.passwordResetTokenExpiresAt = null;
    }

    public void deactivate() {
        this.active = false;
    }

    public void markPasswordReset(String token, Instant expiresAt) {
        this.passwordResetToken = token;
        this.passwordResetTokenExpiresAt = expiresAt;
    }
}
