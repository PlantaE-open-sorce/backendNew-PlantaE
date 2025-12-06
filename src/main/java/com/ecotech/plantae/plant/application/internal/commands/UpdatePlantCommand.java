package com.ecotech.plantae.plant.application.internal.commands;

public record UpdatePlantCommand(
                String ownerId,
                String plantId,
                String name,
                String species,
                String status,
                String location,
                String sensorId) {
}
