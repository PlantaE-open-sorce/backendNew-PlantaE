package com.ecotech.plantae.alert.application.internal.queries;

public record GetRecentAlertsQuery(
        String ownerId,
        String plantId,
        String sensorId,
        String type,
        int page,
        int size,
        String acceptLanguage
) {}
