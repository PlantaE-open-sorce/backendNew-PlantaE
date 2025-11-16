package com.ecotech.plantae.plant.infrastructure.persistence.repository;

import com.ecotech.plantae.plant.infrastructure.persistence.PlantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SpringDataPlantRepository extends JpaRepository<PlantJpaEntity, String>, JpaSpecificationExecutor<PlantJpaEntity> {
    Optional<PlantJpaEntity> findByIdAndOwnerId(String id, String ownerId);
}
