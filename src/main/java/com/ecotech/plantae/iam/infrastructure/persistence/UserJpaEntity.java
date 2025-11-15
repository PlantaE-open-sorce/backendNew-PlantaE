package com.ecotech.plantae.iam.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "users")
public class UserJpaEntity {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false, length = 5)
    private String language;

    @Column(nullable = false, length = 10)
    private String role;

    @Column(nullable = false, length = 40)
    private String accountType;

    @Column(nullable = false)
    private boolean active;

    private String passwordResetToken;

    private Instant passwordResetTokenExpiresAt;

    protected UserJpaEntity() {
        // JPA only
    }

    public UserJpaEntity(String id,
                         String email,
                         String passwordHash,
                         String displayName,
                         String language,
                         String role,
                         String accountType,
                         boolean active,
                         String passwordResetToken,
                         Instant passwordResetTokenExpiresAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.displayName = displayName;
        this.language = language;
        this.role = role;
        this.accountType = accountType;
        this.active = active;
        this.passwordResetToken = passwordResetToken;
        this.passwordResetTokenExpiresAt = passwordResetTokenExpiresAt;
    }

    public String getId() {
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

    public String getLanguage() {
        return language;
    }

    public String getRole() {
        return role;
    }

    public String getAccountType() {
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
}
