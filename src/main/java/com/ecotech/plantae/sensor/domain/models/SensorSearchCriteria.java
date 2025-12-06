package com.ecotech.plantae.sensor.domain.models;

import com.ecotech.plantae.sensor.domain.valueobjects.SensorStatus;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorType;

public record SensorSearchCriteria(
                String ownerId,
                SensorType type,
                SensorStatus status,
                String plantId,
                int page,
                int size) {
}
