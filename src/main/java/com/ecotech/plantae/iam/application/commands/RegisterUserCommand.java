package com.ecotech.plantae.iam.application.commands;

public record RegisterUserCommand(String email, String password, String displayName, String language, String accountType) {
}
