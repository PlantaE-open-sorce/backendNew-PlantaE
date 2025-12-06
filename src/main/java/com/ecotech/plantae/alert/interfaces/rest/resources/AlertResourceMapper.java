package com.ecotech.plantae.alert.interfaces.rest.resources;

import com.ecotech.plantae.alert.domain.entities.Alert;
import com.ecotech.plantae.plant.domain.entities.Plant;
import com.ecotech.plantae.sensor.domain.entities.Sensor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AlertResourceMapper {

    public AlertResource toResource(Alert alert,
            Map<String, Plant> plantById,
            Map<String, Sensor> sensorById) {
        String plantIdStr = alert.getPlantId() != null ? alert.getPlantId().value() : null;
        String sensorIdStr = alert.getSensorId() != null ? alert.getSensorId().value() : null;

        Plant plant = plantIdStr != null ? plantById.get(plantIdStr) : null;
        Sensor sensor = sensorIdStr != null ? sensorById.get(sensorIdStr) : null;

        Map<String, Object> metadata = alert.getMetadata();
        Double value = null;
        String metric = null;
        String message = null;

        if (metadata != null) {
            Object valueObj = metadata.get("value");
            if (valueObj instanceof Number) {
                value = ((Number) valueObj).doubleValue();
            }
            Object metricObj = metadata.get("metric");
            if (metricObj instanceof String) {
                metric = (String) metricObj;
            }
            Object messageObj = metadata.get("message");
            if (messageObj instanceof String) {
                message = (String) messageObj;
            }
        }

        return AlertResource.builder()
                .id(alert.getId().value())
                .plantId(plantIdStr)
                .plantName(plant != null ? plant.getName() : null)
                .plantSpecies(plant != null ? plant.getSpecies() : null)
                .sensorId(sensorIdStr)
                .sensorType(sensor != null ? sensor.getType().name() : null)
                .type(alert.getType().name())
                .status(alert.getStatus().name())
                .message(message)
                .occurredAt(alert.getOccurredAt())
                .resolvedAt(alert.getResolvedAt())
                .value(value)
                .metric(metric)
                .build();
    }
}
