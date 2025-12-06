package com.ecotech.plantae.iam.application.internal.outboundservices;

import java.util.Locale;

public interface PasswordResetNotifier {
    void send(String email, String token, Locale locale);
}
