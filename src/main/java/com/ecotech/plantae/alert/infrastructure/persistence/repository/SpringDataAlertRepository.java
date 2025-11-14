package com.ecotech.plantae.alert.infrastructure.persistence.repository;

import com.ecotech.plantae.alert.infrastructure.persistence.AlertJpaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataAlertRepository extends JpaRepository<AlertJpaEntity, String> {

    @Query("select a from AlertJpaEntity a where a.ownerId = :ownerId " +
            "and (:plantId is null or a.plantId = :plantId) " +
            "and (:sensorId is null or a.sensorId = :sensorId) " +
            "and (:type is null or a.type = :type) " +
            "order by a.occurredAt desc")
    List<AlertJpaEntity> findRecent(@Param("ownerId") String ownerId,
                                    @Param("plantId") String plantId,
                                    @Param("sensorId") String sensorId,
                                    @Param("type") String type,
                                    Pageable pageable);

    List<AlertJpaEntity> findByPlantIdOrderByOccurredAtDesc(String plantId, Pageable pageable);

    long countByPlantIdAndStatus(String plantId, String status);
}
