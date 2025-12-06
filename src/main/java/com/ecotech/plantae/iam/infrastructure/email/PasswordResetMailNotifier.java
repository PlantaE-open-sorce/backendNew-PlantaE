package com.ecotech.plantae.iam.infrastructure.email;

import com.ecotech.plantae.iam.application.internal.outboundservices.PasswordResetNotifier;
import com.ecotech.plantae.shared.application.internal.i18n.LocalizedMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Component
public class PasswordResetMailNotifier implements PasswordResetNotifier {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetMailNotifier.class);

    private final JavaMailSender mailSender;
    private final LocalizedMessageService localizedMessageService;
    private final String frontendBaseUrl;
    private final String mailFrom;

    public PasswordResetMailNotifier(JavaMailSender mailSender,
            LocalizedMessageService localizedMessageService,
            @Value("${app.frontend.base-url:http://localhost:4200}") String frontendBaseUrl,
            @Value("${app.mail.from:no-reply@plantae.app}") String mailFrom) {
        this.mailSender = mailSender;
        this.localizedMessageService = localizedMessageService;
        this.frontendBaseUrl = frontendBaseUrl;
        this.mailFrom = mailFrom;
    }

    @Override
    public void send(String email, String token, Locale locale) {
        try {
            log.info("Attempting to send password reset email to: {}", email);
            String resetLink = buildResetLink(email, token);
            String subject = localizedMessageService.getMessage("iam.reset.email.subject", locale);
            String bodyTemplate = localizedMessageService.getMessage("iam.reset.email.body", locale);
            String body = bodyTemplate.replace("{link}", resetLink);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailFrom);
            message.setTo(email);
            message.setSubject(subject);
            message.setText(body);
            log.info("Sending email from: {} to: {}", mailFrom, email);
            mailSender.send(message);
            log.info("Password reset email sent successfully to: {}", email);
        } catch (Exception ex) {
            log.error("Failed to send password reset email to {}: {}", email, ex.getMessage(), ex);
        }
    }

    private String buildResetLink(String email, String token) {
        String base = frontendBaseUrl.endsWith("/") ? frontendBaseUrl.substring(0, frontendBaseUrl.length() - 1)
                : frontendBaseUrl;
        return base + "/reset-password?token=" + urlEncode(token) + "&email=" + urlEncode(email);
    }

    private String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
