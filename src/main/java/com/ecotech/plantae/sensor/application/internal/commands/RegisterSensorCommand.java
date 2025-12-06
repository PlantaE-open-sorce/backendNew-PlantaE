package com.ecotech.plantae.sensor.application.internal.commands;

public record RegisterSensorCommand(String type, String ownerId, String plantId) {}
