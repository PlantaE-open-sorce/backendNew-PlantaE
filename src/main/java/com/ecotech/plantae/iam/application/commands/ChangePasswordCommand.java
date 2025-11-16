package com.ecotech.plantae.iam.application.commands;

public record ChangePasswordCommand(String userId, String currentPassword, String newPassword) {
}
