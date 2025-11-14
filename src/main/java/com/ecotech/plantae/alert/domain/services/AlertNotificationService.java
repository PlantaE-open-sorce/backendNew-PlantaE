package com.ecotech.plantae.alert.domain.services;

import com.ecotech.plantae.alert.domain.entities.Alert;

public interface AlertNotificationService {
    void notify(Alert alert, String acceptLanguageOverride);
}
