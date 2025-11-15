package com.ecotech.plantae.sensor.domain.models;

import java.util.List;

public record PagedResult<T>(List<T> content, long totalElements, int page, int size, int totalPages) {}
