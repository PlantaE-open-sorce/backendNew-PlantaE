package com.ecotech.plantae.sensor.domain.dtos;

public record SensorReadingDto(String sensorId, String timestamp, String metric, double value, String quality) {
}
