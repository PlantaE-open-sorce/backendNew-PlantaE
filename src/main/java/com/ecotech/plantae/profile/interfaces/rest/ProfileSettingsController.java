package com.ecotech.plantae.profile.interfaces.rest;

import com.ecotech.plantae.iam.domain.repositories.UserRepository;
import com.ecotech.plantae.iam.domain.valueobjects.UserId;
import com.ecotech.plantae.profile.application.commands.CreateProfileCommand;
import com.ecotech.plantae.profile.application.commands.UpdateNotificationPreferencesCommand;
import com.ecotech.plantae.profile.application.commands.UpdateProfileDetailsCommand;
import com.ecotech.plantae.profile.application.handlers.CreateProfileHandler;
import com.ecotech.plantae.profile.application.handlers.GetNotificationPreferencesHandler;
import com.ecotech.plantae.profile.application.handlers.GetProfileDetailsHandler;
import com.ecotech.plantae.profile.application.handlers.UpdateNotificationPreferencesHandler;
import com.ecotech.plantae.profile.application.handlers.UpdateProfileDetailsHandler;
import com.ecotech.plantae.profile.domain.dtos.NotificationPreferenceDto;
import com.ecotech.plantae.profile.domain.dtos.NotificationSettingsDto;
import com.ecotech.plantae.profile.domain.dtos.ProfileDetailsDto;
import com.ecotech.plantae.shared.application.i18n.LanguageResolver;
import com.ecotech.plantae.shared.application.i18n.LocalizedMessageService;
import com.ecotech.plantae.shared.application.security.CurrentUserProvider;
import com.ecotech.plantae.shared.interfaces.rest.MessageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/profile")
@Validated
@Tag(name = "BC: Profile Preferences")
@SecurityRequirement(name = "bearerAuth")
public class ProfileSettingsController {

    private final CurrentUserProvider currentUserProvider;
    private final GetProfileDetailsHandler getProfileDetailsHandler;
    private final UpdateProfileDetailsHandler updateProfileDetailsHandler;
    private final GetNotificationPreferencesHandler getNotificationPreferencesHandler;
    private final UpdateNotificationPreferencesHandler updateNotificationPreferencesHandler;
    private final LanguageResolver languageResolver;
    private final LocalizedMessageService localizedMessageService;
    private final CreateProfileHandler createProfileHandler;
    private final UserRepository userRepository;

    public ProfileSettingsController(CurrentUserProvider currentUserProvider,
                                     GetProfileDetailsHandler getProfileDetailsHandler,
                                     UpdateProfileDetailsHandler updateProfileDetailsHandler,
                                     GetNotificationPreferencesHandler getNotificationPreferencesHandler,
                                     UpdateNotificationPreferencesHandler updateNotificationPreferencesHandler,
                                     LanguageResolver languageResolver,
                                     LocalizedMessageService localizedMessageService,
                                     CreateProfileHandler createProfileHandler,
                                     UserRepository userRepository) {
        this.currentUserProvider = currentUserProvider;
        this.getProfileDetailsHandler = getProfileDetailsHandler;
        this.updateProfileDetailsHandler = updateProfileDetailsHandler;
        this.getNotificationPreferencesHandler = getNotificationPreferencesHandler;
        this.updateNotificationPreferencesHandler = updateNotificationPreferencesHandler;
        this.languageResolver = languageResolver;
        this.localizedMessageService = localizedMessageService;
        this.createProfileHandler = createProfileHandler;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<ProfileDetailsResponse> getProfile(@RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        String userId = requireUser();
        ensureProfileExists(userId);
        ProfileDetailsDto dto = getProfileDetailsHandler.handle(userId);
        if (dto == null) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Profile not found");
        }
        Locale locale = languageResolver.resolve(acceptLanguage, dto.language());
        String message = localizedMessageService.getMessage("profile.get.success", locale);
        return ResponseEntity.ok(new ProfileDetailsResponse(message, dto));
    }

    @PutMapping
    public ResponseEntity<MessageResponse> updateProfile(@RequestHeader(value = "Accept-Language", required = false) String acceptLanguage,
                                                         @Valid @RequestBody UpdateProfileRequest request) {
        String userId = requireUser();
        ensureProfileExists(userId);
        updateProfileDetailsHandler.handle(new UpdateProfileDetailsCommand(userId, request.fullName(), request.timezone(), request.language()));
        Locale locale = languageResolver.resolve(acceptLanguage, request.language());
        String message = localizedMessageService.getMessage("profile.update.success", locale);
        return ResponseEntity.ok(new MessageResponse(message));
    }

    @GetMapping("/notifications")
    public ResponseEntity<NotificationSettingsResponse> getNotifications(@RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        String userId = requireUser();
        ensureProfileExists(userId);
        NotificationSettingsDto dto = getNotificationPreferencesHandler.handle(userId);
        if (dto == null) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Profile not found");
        }
        Locale locale = languageResolver.resolve(acceptLanguage, "en");
        String message = localizedMessageService.getMessage("profile.notifications.get.success", locale);
        return ResponseEntity.ok(new NotificationSettingsResponse(message, dto));
    }

    @PutMapping("/notifications")
    public ResponseEntity<MessageResponse> updateNotifications(@RequestHeader(value = "Accept-Language", required = false) String acceptLanguage,
                                                               @Valid @RequestBody UpdateNotificationsRequest request) {
        String userId = requireUser();
        ensureProfileExists(userId);
        updateNotificationPreferencesHandler.handle(new UpdateNotificationPreferencesCommand(
                userId,
                request.quietHoursStart(),
                request.quietHoursEnd(),
                request.digestTime(),
                request.preferences()
        ));
        Locale locale = languageResolver.resolve(acceptLanguage, "en");
        String message = localizedMessageService.getMessage("profile.notifications.update.success", locale);
        return ResponseEntity.ok(new MessageResponse(message));
    }

    private String requireUser() {
        return currentUserProvider.getCurrentUserId()
                .orElseThrow(() -> new ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Unauthorized"));
    }

    private void ensureProfileExists(String userId) {
        userRepository.findById(UserId.of(userId)).ifPresent(user -> {
            try {
                createProfileHandler.handle(new CreateProfileCommand(userId, user.getDisplayName()));
            } catch (IllegalStateException ignored) {
                // Perfil existente, no se requiere acción adicional.
            }
        });
    }

    public record UpdateProfileRequest(
            @NotBlank @Size(max = 150) String fullName,
            @NotBlank String timezone,
            @NotBlank String language
    ) {}

    public record UpdateNotificationsRequest(
            @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$") String quietHoursStart,
            @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$") String quietHoursEnd,
            @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$") String digestTime,
            @Valid List<NotificationPreferenceDto> preferences
    ) {}

    public record ProfileDetailsResponse(String message, ProfileDetailsDto data) {}

    public record NotificationSettingsResponse(String message, NotificationSettingsDto data) {}
}
