package com.ecotech.plantae.alert.interfaces.rest;

import com.ecotech.plantae.alert.application.internal.handlers.GetRecentAlertsHandler;
import com.ecotech.plantae.alert.application.internal.queries.GetRecentAlertsQuery;
import com.ecotech.plantae.alert.application.internal.commands.ResolveAlertCommand;
import com.ecotech.plantae.alert.application.internal.handlers.ResolveAlertHandler;
import com.ecotech.plantae.alert.domain.entities.Alert;
import com.ecotech.plantae.alert.interfaces.rest.resources.AlertResource;
import com.ecotech.plantae.alert.interfaces.rest.resources.AlertResourceMapper;
import com.ecotech.plantae.plant.domain.entities.Plant;
import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;
import com.ecotech.plantae.sensor.domain.entities.Sensor;
import com.ecotech.plantae.sensor.domain.repositories.SensorRepository;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorId;
import com.ecotech.plantae.shared.application.internal.security.CurrentUserProvider;
import com.ecotech.plantae.shared.interfaces.rest.MessageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/alerts")
@Validated
@Tag(name = "BC: Alert")
@SecurityRequirement(name = "bearerAuth")
public class AlertController {

    private final GetRecentAlertsHandler getRecentAlertsHandler;
    private final ResolveAlertHandler resolveAlertHandler;
    private final CurrentUserProvider currentUserProvider;
    private final PlantRepository plantRepository;
    private final SensorRepository sensorRepository;
    private final AlertResourceMapper alertResourceMapper;

    public AlertController(GetRecentAlertsHandler getRecentAlertsHandler,
            ResolveAlertHandler resolveAlertHandler,
            CurrentUserProvider currentUserProvider,
            PlantRepository plantRepository,
            SensorRepository sensorRepository,
            AlertResourceMapper alertResourceMapper) {
        this.getRecentAlertsHandler = getRecentAlertsHandler;
        this.resolveAlertHandler = resolveAlertHandler;
        this.currentUserProvider = currentUserProvider;
        this.plantRepository = plantRepository;
        this.sensorRepository = sensorRepository;
        this.alertResourceMapper = alertResourceMapper;
    }

    @GetMapping("/recent")
    public ResponseEntity<List<AlertResource>> recent(
            @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage,
            @RequestParam(required = false) String plantId,
            @RequestParam(required = false) String sensorId,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        String ownerId = requireUser();
        List<Alert> alerts = getRecentAlertsHandler
                .handle(new GetRecentAlertsQuery(ownerId, plantId, sensorId, type, page, size, acceptLanguage));

        Set<String> plantIds = alerts.stream()
                .filter(a -> a.getPlantId() != null)
                .map(a -> a.getPlantId().value())
                .collect(Collectors.toSet());

        Set<String> sensorIds = alerts.stream()
                .filter(a -> a.getSensorId() != null)
                .map(a -> a.getSensorId().value())
                .collect(Collectors.toSet());

        Map<String, Plant> plantById = new HashMap<>();
        for (String id : plantIds) {
            plantRepository.findById(PlantId.of(id)).ifPresent(plant -> plantById.put(id, plant));
        }

        Map<String, Sensor> sensorById = new HashMap<>();
        for (String id : sensorIds) {
            sensorRepository.findById(SensorId.of(id)).ifPresent(sensor -> sensorById.put(id, sensor));
        }

        List<AlertResource> resources = alerts.stream()
                .map(alert -> alertResourceMapper.toResource(alert, plantById, sensorById))
                .toList();

        return ResponseEntity.ok(resources);
    }

    @PostMapping("/{alertId}/resolve")
    public ResponseEntity<MessageResponse> resolve(@PathVariable String alertId) {
        requireUser();
        try {
            resolveAlertHandler.handle(new ResolveAlertCommand(alertId));
            return ResponseEntity.ok(new MessageResponse("Alert resolved"));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    private String requireUser() {
        return currentUserProvider.getCurrentUserId()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
    }
}
