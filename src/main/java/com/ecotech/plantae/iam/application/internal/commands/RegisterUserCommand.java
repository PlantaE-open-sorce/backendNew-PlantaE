package com.ecotech.plantae.iam.application.internal.commands;

public record RegisterUserCommand(String email, String password, String displayName, String language, String accountType) {
}
