package com.ecotech.plantae.sensor.application.queries;

public record GetSensorReadingsQuery(
        String sensorId,
        String from,
        String to,
        String metric,
        int page,
        int size
) {}
