package com.ecotech.plantae.profile.domain.dtos;

public record ProfileDetailsDto(
        String fullName,
        String timezone,
        String language,
        String displayName,
        String bio,
        String avatarUrl,
        String location,
        String slug
) {}
