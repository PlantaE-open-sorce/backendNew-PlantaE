package com.ecotech.plantae.iam.application.internal.commands;

import java.util.Locale;

public record ForgotPasswordCommand(String email, Locale locale) {
}
