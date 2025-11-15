package com.ecotech.plantae.sensor.domain.valueobjects;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum SensorMetric {
    SOILMOISTURE("soilMoisture"),
    TEMPERATURE("temperature"),
    HUMIDITY("humidity"),
    LIGHT("light");

    private final String apiName;
    private static final Map<String, SensorMetric> LOOKUP;

    static {
        Map<String, SensorMetric> map = new HashMap<>();
        for (SensorMetric metric : values()) {
            map.put(normalize(metric.name()), metric);
            map.put(normalize(metric.apiName), metric);
        }
        LOOKUP = Collections.unmodifiableMap(map);
    }

    SensorMetric(String apiName) {
        this.apiName = apiName;
    }

    public String apiName() {
        return apiName;
    }

    public static SensorMetric from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Metric is required");
        }
        SensorMetric metric = LOOKUP.get(normalize(value));
        if (metric == null) {
            throw new IllegalArgumentException("Unsupported metric: " + value);
        }
        return metric;
    }

    private static String normalize(String value) {
        return value.replaceAll("[^A-Za-z]", "").toLowerCase();
    }
}
