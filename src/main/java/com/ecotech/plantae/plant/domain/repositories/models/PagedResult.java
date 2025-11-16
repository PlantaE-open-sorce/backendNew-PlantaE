package com.ecotech.plantae.plant.domain.repositories.models;

import java.util.List;

public record PagedResult<T>(List<T> content, long totalElements, int page, int size, int totalPages) {
}
