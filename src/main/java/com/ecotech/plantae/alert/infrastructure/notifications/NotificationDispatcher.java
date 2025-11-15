package com.ecotech.plantae.alert.infrastructure.notifications;

public interface NotificationDispatcher {
    void dispatch(String ownerId, String channel, String message);
}
