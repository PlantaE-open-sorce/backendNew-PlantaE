package com.ecotech.plantae.nursery.domain.dto;

public record NurseryInputDto(
        String id,
        String assetId,
        String assetType,
        String inputType,
        double quantity,
        String unit,
        double cost,
        String appliedAt,
        String appliedBy
) {
}
