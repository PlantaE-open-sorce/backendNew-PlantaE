package com.ecotech.plantae.shared.infrastructure.config;

import com.ecotech.plantae.shared.application.internal.i18n.LanguageResolver;
import com.ecotech.plantae.shared.application.internal.i18n.LocalizedMessageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonBeansConfig {

    @Bean
    public LanguageResolver languageResolver() {
        return new LanguageResolver();
    }

    @Bean
    public LocalizedMessageService localizedMessageService() {
        return new LocalizedMessageService();
    }
}
