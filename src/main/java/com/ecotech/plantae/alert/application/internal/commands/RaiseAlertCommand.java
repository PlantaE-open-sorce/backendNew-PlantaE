package com.ecotech.plantae.alert.application.internal.commands;

public record RaiseAlertCommand(
        String plantId,
        String sensorId,
        String ownerId,
        String type,
        String metric,
        double value,
        double threshold,
        String acceptLanguage
) {}
