package com.ecotech.plantae.report.domain.dtos;

import java.util.List;

public record PlantReportRequestDto(String plantId, String from, String to, List<String> metrics) {}
