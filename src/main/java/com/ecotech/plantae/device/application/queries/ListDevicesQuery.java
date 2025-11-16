package com.ecotech.plantae.device.application.queries;

public record ListDevicesQuery(String ownerId, boolean activeOnly) {
}
