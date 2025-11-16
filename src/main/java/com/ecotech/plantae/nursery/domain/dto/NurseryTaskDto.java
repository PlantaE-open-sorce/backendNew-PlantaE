package com.ecotech.plantae.nursery.domain.dto;

public record NurseryTaskDto(
        String id,
        String title,
        String assetId,
        String assetType,
        String dueDate,
        String priority,
        String status,
        String notes
) {
}
