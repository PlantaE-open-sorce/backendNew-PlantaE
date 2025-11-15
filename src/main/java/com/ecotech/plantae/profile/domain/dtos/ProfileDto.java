package com.ecotech.plantae.profile.domain.dtos;

public record ProfileDto(
        String id,
        String ownerId,
        String displayName,
        String bio,
        String avatarUrl,
        String location,
        String timezone,
        String slug
) {
}
