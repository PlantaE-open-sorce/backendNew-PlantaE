package com.ecotech.plantae.profile.infrastructure.persistence;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profiles", uniqueConstraints = {
        @UniqueConstraint(columnNames = "slug"),
        @UniqueConstraint(columnNames = "ownerId")
})
public class ProfileJpaEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String ownerId;

    @Column(nullable = false)
    private String displayName;

    @Column(length = 500)
    private String bio;

    private String avatarUrl;

    private String location;

    private String timezone;

    private String fullName;

    @Column(length = 5)
    private String language;

    private String quietHoursStart;
    private String quietHoursEnd;
    private String digestTime;

    @Column(nullable = false, length = 50)
    private String slug;

    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "profile_notification_preferences", joinColumns = @JoinColumn(name = "profile_id"))
    private List<ProfileNotificationPreferenceEmbeddable> notificationPreferences = new ArrayList<>();

    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "profile_tour_routes", joinColumns = @JoinColumn(name = "profile_id"))
    private List<String> tourCompletedRoutes = new ArrayList<>();

    protected ProfileJpaEntity() {
    }

    public ProfileJpaEntity(String id, String ownerId, String displayName, String bio,
                            String avatarUrl, String location, String timezone, String slug,
                            String fullName, String language, String quietHoursStart,
                            String quietHoursEnd, String digestTime,
                            List<ProfileNotificationPreferenceEmbeddable> notificationPreferences,
                            List<String> tourCompletedRoutes) {
        this.id = id;
        this.ownerId = ownerId;
        this.displayName = displayName;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
        this.location = location;
        this.timezone = timezone;
        this.slug = slug;
        this.fullName = fullName;
        this.language = language;
        this.quietHoursStart = quietHoursStart;
        this.quietHoursEnd = quietHoursEnd;
        this.digestTime = digestTime;
        if (notificationPreferences != null) {
            this.notificationPreferences = new ArrayList<>(notificationPreferences);
        }
        if (tourCompletedRoutes != null) {
            this.tourCompletedRoutes = new ArrayList<>(tourCompletedRoutes);
        }
    }

    public String getId() {
        return id;
    }

    public String getOwnerId() {
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

    public String getSlug() {
        return slug;
    }

    public String getFullName() {
        return fullName;
    }

    public String getLanguage() {
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

    public List<ProfileNotificationPreferenceEmbeddable> getNotificationPreferences() {
        return notificationPreferences;
    }

    public List<String> getTourCompletedRoutes() {
        return tourCompletedRoutes;
    }
}
