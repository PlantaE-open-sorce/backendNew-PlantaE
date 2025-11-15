package com.ecotech.plantae.home.domain.dto;

public record HomePlantCard(
        String id,
        String name,
        String species,
        String status,
        boolean hasAlerts
) {
}
