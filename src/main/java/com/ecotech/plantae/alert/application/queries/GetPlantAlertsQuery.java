package com.ecotech.plantae.alert.application.queries;

public record GetPlantAlertsQuery(String ownerId, String plantId, int page, int size, String acceptLanguage) {}
