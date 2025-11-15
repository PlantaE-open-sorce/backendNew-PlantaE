package com.ecotech.plantae.alert.application.handlers;

import com.ecotech.plantae.alert.domain.dtos.AlertDto;
import com.ecotech.plantae.alert.domain.entities.Alert;
import com.ecotech.plantae.shared.application.i18n.LocalizedMessageService;

import java.util.Locale;

final class AlertDtoMapper {

    private AlertDtoMapper() {}

    static AlertDto toDto(Alert alert, LocalizedMessageService localizedMessageService, Locale locale) {
        String messageKey = "alerts." + alert.getType().name();
        String message = localizedMessageService.getMessage(messageKey + ".title", locale);
        double value = alert.getMetadata().getOrDefault("value", 0d) instanceof Number n ? n.doubleValue() : 0d;
        String metric = alert.getMetadata().getOrDefault("metric", "").toString();
        return new AlertDto(
                alert.getId().value(),
                alert.getPlantId().value(),
                alert.getSensorId() != null ? alert.getSensorId().value() : null,
                alert.getType().name(),
                alert.getStatus().name(),
                message,
                alert.getOccurredAt().toString(),
                alert.getResolvedAt() != null ? alert.getResolvedAt().toString() : null,
                value,
                metric
        );
    }
}
