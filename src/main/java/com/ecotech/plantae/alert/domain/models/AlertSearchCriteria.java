package com.ecotech.plantae.alert.domain.models;

import com.ecotech.plantae.alert.domain.valueobjects.AlertType;

public record AlertSearchCriteria(
        String plantId,
        String sensorId,
        AlertType type,
        int page,
        int size
) {}
