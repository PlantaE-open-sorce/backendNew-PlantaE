package com.ecotech.plantae.nursery.domain.dto;

import com.ecotech.plantae.shared.application.simulation.HardwareProfileGenerator.HardwareSnapshot;

public record NurseryBatchCard(
        String batchId,
        String label,
        String species,
        int plantCount,
        String routineStatus,
        int progressPercent,
        HardwareSnapshot hardwareProfile
) {
}
