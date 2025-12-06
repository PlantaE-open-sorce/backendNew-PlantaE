package com.ecotech.plantae.sensor.application.internal.commands;

public record IngestSensorReadingCommand(
        String sensorId,
        String timestamp,
        String metric,
        double value,
        String quality,
        String acceptLanguage
) {}
