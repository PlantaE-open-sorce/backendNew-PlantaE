package com.ecotech.plantae.profile.application.commands;

public record UpdateProfileCommand(
        String ownerId,
        String displayName,
        String bio,
        String avatarUrl,
        String location,
        String timezone,
        String slug
) {
}
