package com.ecotech.plantae.nursery.domain.dto;

import com.ecotech.plantae.shared.application.simulation.HardwareProfileGenerator.HardwareSnapshot;

import java.util.List;

public record NurseryDashboardResponse(
        int totalPlants,
        int activeBatches,
        int pendingTasks,
        List<NurseryBatchCard> highlightedBatches,
        List<NurseryTaskDto> criticalTasks,
        List<NurseryInputDto> recentInputs,
        List<HardwareSnapshot> hardwareStatus
) {
}
