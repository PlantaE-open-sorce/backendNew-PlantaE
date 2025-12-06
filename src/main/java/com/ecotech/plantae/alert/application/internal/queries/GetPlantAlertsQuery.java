package com.ecotech.plantae.alert.application.internal.queries;

public record GetPlantAlertsQuery(String ownerId, String plantId, int page, int size, String acceptLanguage) {}
