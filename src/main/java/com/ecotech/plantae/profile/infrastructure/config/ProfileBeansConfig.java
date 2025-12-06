package com.ecotech.plantae.profile.infrastructure.config;

import com.ecotech.plantae.profile.application.internal.handlers.CreateProfileHandler;
import com.ecotech.plantae.profile.application.internal.handlers.GetNotificationPreferencesHandler;
import com.ecotech.plantae.profile.application.internal.handlers.GetProfileBySlugHandler;
import com.ecotech.plantae.profile.application.internal.handlers.GetProfileDetailsHandler;
import com.ecotech.plantae.profile.application.internal.handlers.UpdateNotificationPreferencesHandler;
import com.ecotech.plantae.profile.application.internal.handlers.UpdateProfileDetailsHandler;
import com.ecotech.plantae.profile.application.internal.handlers.UpdateProfileHandler;
import com.ecotech.plantae.profile.application.internal.services.SlugService;
import com.ecotech.plantae.profile.domain.repositories.ProfileRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProfileBeansConfig {

    @Bean
    public CreateProfileHandler createProfileHandler(ProfileRepository profileRepository, SlugService slugService) {
        return new CreateProfileHandler(profileRepository, slugService);
    }

    @Bean
    public UpdateProfileHandler updateProfileHandler(ProfileRepository profileRepository) {
        return new UpdateProfileHandler(profileRepository);
    }

    @Bean
    public GetProfileBySlugHandler getProfileBySlugHandler(ProfileRepository profileRepository) {
        return new GetProfileBySlugHandler(profileRepository);
    }

    @Bean
    public GetProfileDetailsHandler getProfileDetailsHandler(ProfileRepository profileRepository) {
        return new GetProfileDetailsHandler(profileRepository);
    }

    @Bean
    public UpdateProfileDetailsHandler updateProfileDetailsHandler(ProfileRepository profileRepository) {
        return new UpdateProfileDetailsHandler(profileRepository);
    }

    @Bean
    public GetNotificationPreferencesHandler getNotificationPreferencesHandler(ProfileRepository profileRepository) {
        return new GetNotificationPreferencesHandler(profileRepository);
    }

    @Bean
    public UpdateNotificationPreferencesHandler updateNotificationPreferencesHandler(ProfileRepository profileRepository) {
        return new UpdateNotificationPreferencesHandler(profileRepository);
    }
}
