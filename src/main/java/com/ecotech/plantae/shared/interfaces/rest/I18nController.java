package com.ecotech.plantae.shared.interfaces.rest;

import com.ecotech.plantae.shared.application.i18n.LanguageResolver;
import com.ecotech.plantae.shared.application.i18n.LocalizedMessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/i18n")
@Tag(name = "BC: I18N")
public class I18nController {

    private final LocalizedMessageService localizedMessageService;
    private final LanguageResolver languageResolver;

    public I18nController(LocalizedMessageService localizedMessageService, LanguageResolver languageResolver) {
        this.localizedMessageService = localizedMessageService;
        this.languageResolver = languageResolver;
    }

    @GetMapping("/catalog")
    public ResponseEntity<Map<String, String>> catalog(@RequestHeader(value = "Accept-Language", required = false) String acceptLanguage,
                                                       @RequestParam String namespace) {
        if (namespace == null || namespace.isBlank()) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Namespace is required");
        }
        Locale locale = languageResolver.resolve(acceptLanguage, "en");
        Map<String, String> catalog = localizedMessageService.getCatalog(namespace, locale);
        return ResponseEntity.ok(catalog);
    }
}
