package com.ecotech.plantae.plant.application.commands;

public record CreatePlantCommand(
        String ownerId,
        String name,
        String species,
        String deviceId,
        String sensorId
) {
}
