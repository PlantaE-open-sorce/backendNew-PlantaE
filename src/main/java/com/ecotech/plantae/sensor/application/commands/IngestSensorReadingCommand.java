package com.ecotech.plantae.sensor.application.commands;

public record IngestSensorReadingCommand(
        String sensorId,
        String timestamp,
        String metric,
        double value,
        String quality
) {}
