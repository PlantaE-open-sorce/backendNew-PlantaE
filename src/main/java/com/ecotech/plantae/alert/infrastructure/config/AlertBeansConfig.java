package com.ecotech.plantae.alert.infrastructure.config;

import com.ecotech.plantae.alert.application.internal.handlers.GetPlantAlertsHandler;
import com.ecotech.plantae.alert.application.internal.handlers.GetRecentAlertsHandler;
import com.ecotech.plantae.alert.application.internal.handlers.RaiseAlertHandler;
import com.ecotech.plantae.alert.application.internal.handlers.ResolveAlertHandler;
import com.ecotech.plantae.alert.domain.repositories.AlertRepository;
import com.ecotech.plantae.alert.domain.services.AlertNotificationService;
import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import com.ecotech.plantae.shared.infrastructure.realtime.RealtimeEmitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlertBeansConfig {

    @Bean
    public RaiseAlertHandler raiseAlertHandler(AlertRepository alertRepository,
            PlantRepository plantRepository,
            AlertNotificationService alertNotificationService,
            RealtimeEmitter realtimeEmitter) {
        return new RaiseAlertHandler(alertRepository, plantRepository, alertNotificationService, realtimeEmitter);
    }

    @Bean
    public ResolveAlertHandler resolveAlertHandler(AlertRepository alertRepository, PlantRepository plantRepository) {
        return new ResolveAlertHandler(alertRepository, plantRepository);
    }

    @Bean
    public GetRecentAlertsHandler getRecentAlertsHandler(AlertRepository alertRepository) {
        return new GetRecentAlertsHandler(alertRepository);
    }

    @Bean
    public GetPlantAlertsHandler getPlantAlertsHandler(AlertRepository alertRepository) {
        return new GetPlantAlertsHandler(alertRepository);
    }
}
