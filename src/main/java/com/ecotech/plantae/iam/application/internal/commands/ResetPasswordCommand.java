package com.ecotech.plantae.iam.application.internal.commands;

public record ResetPasswordCommand(String token, String newPassword) {
}
