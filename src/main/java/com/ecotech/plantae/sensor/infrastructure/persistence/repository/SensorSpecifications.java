package com.ecotech.plantae.sensor.infrastructure.persistence.repository;

import com.ecotech.plantae.sensor.domain.models.SensorSearchCriteria;
import com.ecotech.plantae.sensor.infrastructure.persistence.SensorJpaEntity;
import org.springframework.data.jpa.domain.Specification;

public final class SensorSpecifications {

    private SensorSpecifications() {
    }

    public static Specification<SensorJpaEntity> fromCriteria(SensorSearchCriteria criteria) {
        return Specification.allOf(
                equals("ownerId", criteria.ownerId()),
                equals("type", criteria.type() != null ? criteria.type().name() : null),
                equals("status", criteria.status() != null ? criteria.status().name() : null),
                equals("plantId", criteria.plantId()));
    }

    private static Specification<SensorJpaEntity> equals(String field, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get(field), value);
    }
}
