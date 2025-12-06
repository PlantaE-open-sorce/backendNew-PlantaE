package com.ecotech.plantae.profile.infrastructure.services;

import com.ecotech.plantae.profile.application.internal.services.SlugService;
import org.springframework.stereotype.Service;

@Service
public class SlugServiceImpl implements SlugService {

    @Override
    public String toSlug(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        String normalized = input.trim().toLowerCase();
        normalized = normalized.replaceAll("\\s+", "-");
        normalized = normalized.replaceAll("[^a-z0-9-]", "");
        normalized = normalized.replaceAll("-+", "-");
        normalized = normalized.replaceAll("^-|-$", "");
        if (normalized.length() < 3) {
            normalized = String.format("%s-%d", normalized.isEmpty() ? "profile" : normalized, System.currentTimeMillis());
        }
        if (normalized.length() > 50) {
            normalized = normalized.substring(0, 50);
        }
        return normalized;
    }
}
