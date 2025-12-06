package com.ecotech.plantae.shared.infrastructure.realtime;

public record AlertEventPayload(String id,
                                String plantId,
                                String sensorId,
                                String type,
                                String status,
                                String occurredAt) {
}
