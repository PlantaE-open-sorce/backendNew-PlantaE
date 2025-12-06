package com.ecotech.plantae.plant.infrastructure.persistence.repository;

import com.ecotech.plantae.plant.domain.repositories.models.PlantSearchCriteria;
import com.ecotech.plantae.plant.infrastructure.persistence.PlantJpaEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public final class PlantSpecifications {

    private PlantSpecifications() {}

    public static Specification<PlantJpaEntity> fromCriteria(PlantSearchCriteria criteria) {
        return Specification.allOf(
                notDeleted(),
                equals("ownerId", criteria.ownerId()),
                like("name", criteria.name()),
                like("species", criteria.species()),
                like("location", criteria.location()),
                equals("status", criteria.status() != null ? criteria.status().name() : null),
                instantGreaterOrEqual("createdAt", criteria.createdFrom()),
                instantLessOrEqual("createdAt", criteria.createdTo()),
                booleanEquals("hasAlerts", criteria.hasAlerts()),
                equals("sensorId", criteria.sensorId())
        );
    }

    private static Specification<PlantJpaEntity> like(String field, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return (root, query, cb) -> cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%");
    }

    private static Specification<PlantJpaEntity> equals(String field, String value) {
        if (value == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get(field), value);
    }

    private static Specification<PlantJpaEntity> booleanEquals(String field, Boolean value) {
        if (value == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get(field), value);
    }

    private static Specification<PlantJpaEntity> instantGreaterOrEqual(String field, Instant value) {
        if (value == null) {
            return null;
        }
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(field), value);
    }

    private static Specification<PlantJpaEntity> instantLessOrEqual(String field, Instant value) {
        if (value == null) {
            return null;
        }
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(field), value);
    }

    private static Specification<PlantJpaEntity> notDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }
}
