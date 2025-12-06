package com.ecotech.plantae.shared.infrastructure.realtime;

public record ReadingEventPayload(String sensorId,
                                  String plantId,
                                  String metric,
                                  double value,
                                  String timestamp) {
}
