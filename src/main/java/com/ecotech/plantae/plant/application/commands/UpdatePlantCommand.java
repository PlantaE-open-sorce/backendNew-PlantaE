package com.ecotech.plantae.plant.application.commands;

public record UpdatePlantCommand(
        String ownerId,
        String plantId,
        String name,
        String species,
        String status,
        String deviceId,
        String sensorId
) {}
