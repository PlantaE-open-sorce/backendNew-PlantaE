package com.ecotech.plantae.sensor.application.commands;

public record RegisterSensorCommand(String type, String ownerId, String plantId) {}
