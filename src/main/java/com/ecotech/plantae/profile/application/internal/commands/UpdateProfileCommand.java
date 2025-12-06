package com.ecotech.plantae.profile.application.internal.commands;

public record UpdateProfileCommand(
        String ownerId,
        String displayName,
        String bio,
        String avatarUrl,
        String location,
        String timezone,
        String slug,
        java.util.List<String> tourCompletedRoutes
) {
}
