package com.ecotech.plantae.alert.infrastructure.config;

import com.ecotech.plantae.alert.application.handlers.GetPlantAlertsHandler;
import com.ecotech.plantae.alert.application.handlers.GetRecentAlertsHandler;
import com.ecotech.plantae.alert.application.handlers.RaiseAlertHandler;
import com.ecotech.plantae.alert.application.handlers.ResolveAlertHandler;
import com.ecotech.plantae.alert.domain.repositories.AlertRepository;
import com.ecotech.plantae.alert.domain.services.AlertNotificationService;
import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import com.ecotech.plantae.shared.application.i18n.LocalizedMessageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlertBeansConfig {

    @Bean
    public RaiseAlertHandler raiseAlertHandler(AlertRepository alertRepository,
                                               PlantRepository plantRepository,
                                               AlertNotificationService alertNotificationService) {
        return new RaiseAlertHandler(alertRepository, plantRepository, alertNotificationService);
    }

    @Bean
    public ResolveAlertHandler resolveAlertHandler(AlertRepository alertRepository, PlantRepository plantRepository) {
        return new ResolveAlertHandler(alertRepository, plantRepository);
    }

    @Bean
    public GetRecentAlertsHandler getRecentAlertsHandler(AlertRepository alertRepository, LocalizedMessageService localizedMessageService) {
        return new GetRecentAlertsHandler(alertRepository, localizedMessageService);
    }

    @Bean
    public GetPlantAlertsHandler getPlantAlertsHandler(AlertRepository alertRepository, LocalizedMessageService localizedMessageService) {
        return new GetPlantAlertsHandler(alertRepository, localizedMessageService);
    }
}
