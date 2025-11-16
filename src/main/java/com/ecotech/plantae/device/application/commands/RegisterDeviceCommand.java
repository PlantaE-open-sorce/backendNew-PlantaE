package com.ecotech.plantae.device.application.commands;

public record RegisterDeviceCommand(String deviceId, String ownerId, String hwModel, String secret) {
}
