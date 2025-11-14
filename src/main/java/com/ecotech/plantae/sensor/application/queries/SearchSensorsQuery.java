package com.ecotech.plantae.sensor.application.queries;

public record SearchSensorsQuery(String type, String status, String plantId, int page, int size) {}
