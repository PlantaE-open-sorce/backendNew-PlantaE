package com.ecotech.plantae.device.domain.dtos;

public record DeviceDto(String id, String ownerId, String hwModel, boolean active, String note) {
}
