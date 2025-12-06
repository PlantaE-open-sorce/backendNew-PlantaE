package com.ecotech.plantae.profile.infrastructure.persistence.mappers;

import com.ecotech.plantae.iam.domain.valueobjects.UserId;
import com.ecotech.plantae.iam.domain.valueobjects.UserLanguage;
import com.ecotech.plantae.profile.domain.entities.NotificationPreference;
import com.ecotech.plantae.profile.domain.entities.Profile;
import com.ecotech.plantae.profile.domain.valueobjects.ProfileId;
import com.ecotech.plantae.profile.domain.valueobjects.ProfileSlug;
import com.ecotech.plantae.profile.infrastructure.persistence.ProfileJpaEntity;
import com.ecotech.plantae.profile.infrastructure.persistence.ProfileNotificationPreferenceEmbeddable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@SuppressWarnings("null")
public class ProfileJpaMapper {

    public @NonNull ProfileJpaEntity toJpa(@NonNull Profile profile) {
        return new ProfileJpaEntity(
                profile.getId().value(),
                profile.getOwnerId().value(),
                profile.getDisplayName(),
                profile.getBio(),
                profile.getAvatarUrl(),
                profile.getLocation(),
                profile.getTimezone(),
                profile.getSlug().value(),
                profile.getFullName(),
                profile.getLanguage() != null ? profile.getLanguage().name().toLowerCase() : null,
                profile.getQuietHoursStart(),
                profile.getQuietHoursEnd(),
                profile.getDigestTime(),
                profile.getNotificationPreferences().stream()
                        .map(pref -> new ProfileNotificationPreferenceEmbeddable(pref.getType(), pref.isEmailEnabled(), pref.isInAppEnabled()))
                        .collect(Collectors.toList()),
                profile.getTourCompletedRoutes()
        );
    }

    public @NonNull Profile toDomain(@NonNull ProfileJpaEntity entity) {
        return Profile.restore(
                ProfileId.of(entity.getId()),
                UserId.of(entity.getOwnerId()),
                entity.getDisplayName(),
                entity.getBio(),
                entity.getAvatarUrl(),
                entity.getLocation(),
                entity.getTimezone(),
                ProfileSlug.of(entity.getSlug()),
                entity.getFullName(),
                UserLanguage.from(entity.getLanguage()),
                entity.getQuietHoursStart(),
                entity.getQuietHoursEnd(),
                entity.getDigestTime(),
                entity.getNotificationPreferences().stream()
                        .map(pref -> new NotificationPreference(pref.getType(), pref.isEmailEnabled(), pref.isInAppEnabled()))
                        .collect(Collectors.toList()),
                entity.getTourCompletedRoutes()
        );
    }
}
