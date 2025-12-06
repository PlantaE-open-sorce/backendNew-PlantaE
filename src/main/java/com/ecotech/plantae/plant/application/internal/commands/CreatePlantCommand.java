package com.ecotech.plantae.plant.application.internal.commands;

public record CreatePlantCommand(
                String ownerId,
                String name,
                String species,
                String location,
                String sensorId) {
}
