package com.ecotech.plantae.shared.interfaces.rest;

import com.ecotech.plantae.shared.application.internal.i18n.LanguageResolver;
import com.ecotech.plantae.shared.application.internal.i18n.LocalizedMessageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final LocalizedMessageService localizedMessageService;
    private final LanguageResolver languageResolver;

    public GlobalExceptionHandler(LocalizedMessageService localizedMessageService, LanguageResolver languageResolver) {
        this.localizedMessageService = localizedMessageService;
        this.languageResolver = languageResolver;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) String acceptLanguage) {

        Locale locale = languageResolver.resolve(acceptLanguage, "en");

        String fieldMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> resolveFieldError(error.getField(), error.getCode(), locale))
                .findFirst()
                .orElse(localizedMessageService.getMessage("errors.validation", locale));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(fieldMessage));
    }

    private String resolveFieldError(String field, String code, Locale locale) {
        String key = "errors." + field + "." + (code == null ? "invalid" : code.toLowerCase(Locale.ROOT));
        String resolved = localizedMessageService.getMessage(key, locale);
        if (resolved.equals(key)) {
            return localizedMessageService.getMessage("errors.validation", locale);
        }
        return resolved;
    }
}
