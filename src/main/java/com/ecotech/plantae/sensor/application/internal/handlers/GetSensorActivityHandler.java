package com.ecotech.plantae.sensor.application.internal.handlers;

import com.ecotech.plantae.sensor.application.internal.queries.GetSensorActivityQuery;
import com.ecotech.plantae.sensor.domain.models.SensorActivity;
import com.ecotech.plantae.sensor.domain.repositories.SensorReadingRepository;

import java.time.Instant;
import java.util.List;

public class GetSensorActivityHandler {

    private final SensorReadingRepository sensorReadingRepository;

    public GetSensorActivityHandler(SensorReadingRepository sensorReadingRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
    }

    public List<SensorActivity> handle(GetSensorActivityQuery query) {
        Instant from = parseInstant(query.from());
        Instant to = parseInstant(query.to());
        int top = query.top() > 0 ? query.top() : 10;
        return sensorReadingRepository.mostActive(from, to, top);
    }

    private Instant parseInstant(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Instant.parse(value);
    }
}
