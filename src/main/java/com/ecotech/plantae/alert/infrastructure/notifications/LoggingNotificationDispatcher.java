package com.ecotech.plantae.alert.infrastructure.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingNotificationDispatcher implements NotificationDispatcher {

    private static final Logger log = LoggerFactory.getLogger(LoggingNotificationDispatcher.class);

    @Override
    public void dispatch(String ownerId, String channel, String message) {
        log.debug("Dispatching {} notification to {}: {}", channel, ownerId, message);
    }
}
