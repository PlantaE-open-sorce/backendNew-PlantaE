package com.ecotech.plantae.shared.application.i18n;

import java.util.Locale;

public class LanguageResolver {

    public Locale resolve(String acceptLanguageHeader, String storedLanguage) {
        if (acceptLanguageHeader != null && !acceptLanguageHeader.isBlank()) {
            return Locale.forLanguageTag(acceptLanguageHeader.split(",")[0].trim());
        }
        if (storedLanguage != null && !storedLanguage.isBlank()) {
            return Locale.forLanguageTag(storedLanguage);
        }
        return Locale.ENGLISH;
    }
}
