package com.ecotech.plantae.alert.infrastructure.notifications;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingNotificationRepository extends JpaRepository<PendingNotificationJpaEntity, String> {
}
