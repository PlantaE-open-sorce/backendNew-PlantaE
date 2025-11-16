package com.ecotech.plantae.sensor.application.handlers;

import com.ecotech.plantae.sensor.application.queries.SearchSensorsQuery;
import com.ecotech.plantae.sensor.domain.dtos.SensorDto;
import com.ecotech.plantae.sensor.domain.repositories.SensorRepository;
import com.ecotech.plantae.sensor.domain.models.PagedResult;
import com.ecotech.plantae.sensor.domain.models.SensorSearchCriteria;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorStatus;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorType;

public class SearchSensorsHandler {

    private final SensorRepository sensorRepository;

    public SearchSensorsHandler(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public PagedResult<SensorDto> handle(SearchSensorsQuery query) {
        SensorSearchCriteria criteria = new SensorSearchCriteria(
                query.type() != null ? SensorType.valueOf(query.type().toUpperCase()) : null,
                query.status() != null ? SensorStatus.valueOf(query.status().toUpperCase()) : null,
                query.plantId(),
                query.page(),
                query.size()
        );
        PagedResult<com.ecotech.plantae.sensor.domain.entities.Sensor> result = sensorRepository.search(criteria);
        return new PagedResult<>(
                result.content().stream().map(SensorDtoMapper::toDto).toList(),
                result.totalElements(),
                result.page(),
                result.size(),
                result.totalPages()
        );
    }
}
