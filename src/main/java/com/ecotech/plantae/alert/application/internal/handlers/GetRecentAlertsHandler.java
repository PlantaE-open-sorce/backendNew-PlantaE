package com.ecotech.plantae.alert.application.internal.handlers;

import com.ecotech.plantae.alert.application.internal.queries.GetRecentAlertsQuery;
import com.ecotech.plantae.alert.domain.entities.Alert;
import com.ecotech.plantae.alert.domain.models.AlertSearchCriteria;
import com.ecotech.plantae.alert.domain.repositories.AlertRepository;
import com.ecotech.plantae.alert.domain.valueobjects.AlertType;

import java.util.List;

public class GetRecentAlertsHandler {

    private final AlertRepository alertRepository;

    public GetRecentAlertsHandler(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public List<Alert> handle(GetRecentAlertsQuery query) {
        AlertSearchCriteria criteria = new AlertSearchCriteria(
                query.plantId(),
                query.sensorId(),
                query.type() != null ? AlertType.valueOf(query.type().toUpperCase()) : null,
                query.page(),
                query.size());
        return alertRepository.findRecent(query.ownerId(), criteria);
    }
}
