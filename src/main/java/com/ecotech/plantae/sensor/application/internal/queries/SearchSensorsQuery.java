package com.ecotech.plantae.sensor.application.internal.queries;

public record SearchSensorsQuery(String ownerId, String type, String status, String plantId, int page, int size) {
}
