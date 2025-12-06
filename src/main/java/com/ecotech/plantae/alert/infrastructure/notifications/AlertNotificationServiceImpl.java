package com.ecotech.plantae.alert.infrastructure.notifications;

import com.ecotech.plantae.alert.domain.entities.Alert;
import com.ecotech.plantae.alert.domain.services.AlertNotificationService;
import com.ecotech.plantae.alert.domain.valueobjects.AlertType;
import com.ecotech.plantae.profile.domain.entities.Profile;
import com.ecotech.plantae.profile.domain.repositories.ProfileRepository;
import com.ecotech.plantae.profile.domain.valueobjects.NotificationChannel;
import com.ecotech.plantae.profile.domain.valueobjects.NotificationType;
import com.ecotech.plantae.iam.domain.valueobjects.UserId;
import com.ecotech.plantae.shared.application.internal.i18n.LanguageResolver;
import com.ecotech.plantae.shared.application.internal.i18n.LocalizedMessageService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Optional;

@Service
public class AlertNotificationServiceImpl implements AlertNotificationService {

    private final ProfileRepository profileRepository;
    private final LocalizedMessageService localizedMessageService;
    private final LanguageResolver languageResolver;
    private final NotificationDispatcher dispatcher;
    private final PendingNotificationRepository pendingNotificationRepository;

    public AlertNotificationServiceImpl(ProfileRepository profileRepository,
                                        LocalizedMessageService localizedMessageService,
                                        LanguageResolver languageResolver,
                                        NotificationDispatcher dispatcher,
                                        PendingNotificationRepository pendingNotificationRepository) {
        this.profileRepository = profileRepository;
        this.localizedMessageService = localizedMessageService;
        this.languageResolver = languageResolver;
        this.dispatcher = dispatcher;
        this.pendingNotificationRepository = pendingNotificationRepository;
    }

    @Override
    public void notify(Alert alert, String acceptLanguageOverride) {
        Optional<Profile> profileOpt = profileRepository.findByOwnerId(UserId.of(alert.getOwnerId()));
        Profile profile = profileOpt.orElse(null);
        String language = profile != null ? profile.getLanguage().name().toLowerCase() : "en";
        Locale locale = languageResolver.resolve(acceptLanguageOverride, language);
        String message = localizedMessageService.getMessage("alerts." + alert.getType().name() + ".title", locale);

        NotificationType notificationType = mapType(alert.getType());
        if (profile == null) {
            dispatcher.dispatch(alert.getOwnerId(), NotificationChannel.EMAIL.name(), message);
            return;
        }
        boolean emailEnabled = isChannelEnabled(profile, notificationType, NotificationChannel.EMAIL);
        boolean inAppEnabled = isChannelEnabled(profile, notificationType, NotificationChannel.IN_APP);
        ZoneId zoneId = profile.getTimezone() != null ? ZoneId.of(profile.getTimezone()) : ZoneId.of("UTC");
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        boolean withinQuiet = isWithinQuietHours(profile, now.toLocalTime());

        if (emailEnabled) {
            handleChannel(alert.getOwnerId(), NotificationChannel.EMAIL, message, withinQuiet, now, profile);
        }
        if (inAppEnabled) {
            handleChannel(alert.getOwnerId(), NotificationChannel.IN_APP, message, withinQuiet, now, profile);
        }
    }

    private NotificationType mapType(AlertType alertType) {
        try {
            return NotificationType.valueOf(alertType.name());
        } catch (IllegalArgumentException ex) {
            return NotificationType.THRESHOLD_BREACH;
        }
    }

    private boolean isChannelEnabled(Profile profile, NotificationType type, NotificationChannel channel) {
        return profile.getNotificationPreferences().stream()
                .filter(pref -> pref.getType() == type)
                .findFirst()
                .map(pref -> channel == NotificationChannel.EMAIL ? pref.isEmailEnabled() : pref.isInAppEnabled())
                .orElse(true);
    }

    private boolean isWithinQuietHours(Profile profile, LocalTime now) {
        if (profile.getQuietHoursStart() == null || profile.getQuietHoursEnd() == null) {
            return false;
        }
        LocalTime start = LocalTime.parse(profile.getQuietHoursStart());
        LocalTime end = LocalTime.parse(profile.getQuietHoursEnd());
        if (start.isBefore(end)) {
            return !now.isBefore(start) && now.isBefore(end);
        }
        return !now.isBefore(start) || now.isBefore(end);
    }

    private void handleChannel(String ownerId,
                               NotificationChannel channel,
                               String message,
                               boolean withinQuiet,
                               ZonedDateTime now,
                               Profile profile) {
        if (!withinQuiet) {
            dispatcher.dispatch(ownerId, channel.name(), message);
            return;
        }
        LocalTime end = LocalTime.parse(profile.getQuietHoursEnd());
        ZoneId zoneId = profile.getTimezone() != null ? ZoneId.of(profile.getTimezone()) : ZoneId.of("UTC");
        ZonedDateTime nextSend = ZonedDateTime.of(LocalDate.now(zoneId), end, zoneId);
        if (!nextSend.isAfter(now)) {
            nextSend = nextSend.plusDays(1);
        }
        pendingNotificationRepository.save(new PendingNotificationJpaEntity(ownerId,
                PendingNotificationJpaEntity.Channel.valueOf(channel.name()),
                message,
                nextSend.toInstant()));
    }
}
