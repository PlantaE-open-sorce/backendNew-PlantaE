package com.ecotech.plantae.home.domain.dto;

import com.ecotech.plantae.shared.application.simulation.HardwareProfileGenerator.HardwareSnapshot;

import java.util.List;

public record HomeDashboardResponse(
        String greeting,
        String tipOfTheDay,
        List<HomePlantCard> plants,
        List<HomeManualActionDto> recentActions,
        List<HardwareSnapshot> hardwareStatus
) {
}
