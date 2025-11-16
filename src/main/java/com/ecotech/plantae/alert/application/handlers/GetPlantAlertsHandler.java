package com.ecotech.plantae.alert.application.handlers;

import com.ecotech.plantae.alert.application.queries.GetPlantAlertsQuery;
import com.ecotech.plantae.alert.domain.dtos.AlertDto;
import com.ecotech.plantae.alert.domain.repositories.AlertRepository;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;
import com.ecotech.plantae.shared.application.i18n.LocalizedMessageService;

import java.util.List;
import java.util.Locale;

public class GetPlantAlertsHandler {

    private final AlertRepository alertRepository;
    private final LocalizedMessageService localizedMessageService;

    public GetPlantAlertsHandler(AlertRepository alertRepository, LocalizedMessageService localizedMessageService) {
        this.alertRepository = alertRepository;
        this.localizedMessageService = localizedMessageService;
    }

    public List<AlertDto> handle(GetPlantAlertsQuery query) {
        Locale locale = Locale.forLanguageTag(query.acceptLanguage() == null ? "en" : query.acceptLanguage());
        return alertRepository.findByPlant(PlantId.of(query.plantId()), query.page(), query.size()).stream()
                .filter(alert -> alert.getOwnerId().equals(query.ownerId()))
                .map(alert -> AlertDtoMapper.toDto(alert, localizedMessageService, locale))
                .toList();
    }
}
