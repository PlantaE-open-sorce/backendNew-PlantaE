package com.ecotech.plantae.profile.domain.entities;

import com.ecotech.plantae.iam.domain.valueobjects.UserId;
import com.ecotech.plantae.iam.domain.valueobjects.UserLanguage;
import com.ecotech.plantae.profile.domain.valueobjects.NotificationChannel;
import com.ecotech.plantae.profile.domain.valueobjects.NotificationType;
import com.ecotech.plantae.profile.domain.valueobjects.ProfileId;
import com.ecotech.plantae.profile.domain.valueobjects.ProfileSlug;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class Profile {

    private final ProfileId id;
    private final UserId ownerId;
    private String displayName;
    private String bio;
    private String avatarUrl;
    private String location;
    private String timezone;
    private String fullName;
    private UserLanguage language;
    private String quietHoursStart;
    private String quietHoursEnd;
    private String digestTime;
    private final List<NotificationPreference> notificationPreferences = new ArrayList<>();
    private ProfileSlug slug;

    private Profile(ProfileId id, UserId ownerId, String displayName, ProfileSlug slug) {
        this.id = Objects.requireNonNull(id);
        this.ownerId = Objects.requireNonNull(ownerId);
        this.displayName = requireNonBlank(displayName, "Display name is required");
        this.slug = Objects.requireNonNull(slug);
        this.fullName = displayName;
        this.language = UserLanguage.EN;
    }

    public static Profile create(UserId ownerId, String displayName, Function<String, ProfileSlug> slugFactory) {
        Objects.requireNonNull(slugFactory, "Slug factory is required");
        Objects.requireNonNull(displayName, "Display name is required");
        ProfileSlug slug = slugFactory.apply(displayName);
        return new Profile(ProfileId.newId(), ownerId, displayName, slug);
    }

    public static Profile restore(ProfileId id, UserId ownerId, String displayName, String bio,
                                  String avatarUrl, String location, String timezone, ProfileSlug slug,
                                  String fullName, UserLanguage language, String quietStart,
                                  String quietEnd, String digestTime, List<NotificationPreference> preferences) {
        Profile profile = new Profile(id, ownerId, displayName, slug);
        profile.bio = bio;
        profile.avatarUrl = avatarUrl;
        profile.location = location;
        profile.timezone = timezone;
        profile.fullName = fullName != null ? fullName : displayName;
        profile.language = language != null ? language : UserLanguage.EN;
        profile.quietHoursStart = quietStart;
        profile.quietHoursEnd = quietEnd;
        profile.digestTime = digestTime;
        if (preferences != null) {
            profile.notificationPreferences.clear();
            profile.notificationPreferences.addAll(preferences);
        }
        return profile;
    }

    public void updatePublicFields(String displayName, String bio, String avatarUrl, String location, String timezone) {
        if (displayName != null) {
            this.displayName = requireNonBlank(displayName, "Display name cannot be blank");
        }
        if (bio != null) {
            if (bio.length() > 500) {
                throw new IllegalArgumentException("Bio cannot exceed 500 characters");
            }
            this.bio = bio;
        }
        if (avatarUrl != null) {
            this.avatarUrl = avatarUrl;
        }
        if (location != null) {
            this.location = location;
        }
        if (timezone != null) {
            this.timezone = timezone;
        }
    }

    private String requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    public ProfileId getId() {
        return id;
    }

    public UserId getOwnerId() {
        return ownerId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBio() {
        return bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getLocation() {
        return location;
    }

    public String getTimezone() {
        return timezone;
    }

    public ProfileSlug getSlug() {
        return slug;
    }

    public void setSlug(ProfileSlug slug) {
        this.slug = Objects.requireNonNull(slug);
    }

    public String getFullName() {
        return fullName;
    }

    public UserLanguage getLanguage() {
        return language;
    }

    public String getQuietHoursStart() {
        return quietHoursStart;
    }

    public String getQuietHoursEnd() {
        return quietHoursEnd;
    }

    public String getDigestTime() {
        return digestTime;
    }

    public List<NotificationPreference> getNotificationPreferences() {
        return notificationPreferences;
    }

    public void updateProfileDetails(String fullName, String timezone, UserLanguage language) {
        if (fullName != null) {
            this.fullName = requireNonBlank(fullName, "Full name is required");
            this.displayName = this.fullName;
        }
        if (timezone != null) {
            this.timezone = timezone;
        }
        if (language != null) {
            this.language = language;
        }
    }

    public void updateNotificationPreferences(String quietStart,
                                              String quietEnd,
                                              String digest,
                                              List<NotificationPreference> preferences) {
        this.quietHoursStart = quietStart;
        this.quietHoursEnd = quietEnd;
        this.digestTime = digest;
        if (preferences != null) {
            this.notificationPreferences.clear();
            this.notificationPreferences.addAll(preferences);
        }
    }

    public void toggleChannel(NotificationType type, NotificationChannel channel, boolean enabled) {
        Optional<NotificationPreference> pref = notificationPreferences.stream()
                .filter(p -> p.getType() == type)
                .findFirst();
        NotificationPreference target = pref.orElseGet(() -> {
            NotificationPreference np = new NotificationPreference(type, false, false);
            notificationPreferences.add(np);
            return np;
        });
        target.setChannel(channel, enabled);
    }
}
