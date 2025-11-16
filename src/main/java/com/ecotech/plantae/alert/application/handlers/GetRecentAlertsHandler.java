package com.ecotech.plantae.alert.application.handlers;

import com.ecotech.plantae.alert.application.queries.GetRecentAlertsQuery;
import com.ecotech.plantae.alert.domain.dtos.AlertDto;
import com.ecotech.plantae.alert.domain.models.AlertSearchCriteria;
import com.ecotech.plantae.alert.domain.repositories.AlertRepository;
import com.ecotech.plantae.alert.domain.valueobjects.AlertType;
import com.ecotech.plantae.shared.application.i18n.LocalizedMessageService;

import java.util.List;
import java.util.Locale;

public class GetRecentAlertsHandler {

    private final AlertRepository alertRepository;
    private final LocalizedMessageService localizedMessageService;

    public GetRecentAlertsHandler(AlertRepository alertRepository, LocalizedMessageService localizedMessageService) {
        this.alertRepository = alertRepository;
        this.localizedMessageService = localizedMessageService;
    }

    public List<AlertDto> handle(GetRecentAlertsQuery query) {
        AlertSearchCriteria criteria = new AlertSearchCriteria(
                query.plantId(),
                query.sensorId(),
                query.type() != null ? AlertType.valueOf(query.type().toUpperCase()) : null,
                query.page(),
                query.size()
        );
        Locale locale = Locale.forLanguageTag(query.acceptLanguage() == null ? "en" : query.acceptLanguage());
        return alertRepository.findRecent(query.ownerId(), criteria).stream()
                .map(alert -> AlertDtoMapper.toDto(alert, localizedMessageService, locale))
                .toList();
    }
}
