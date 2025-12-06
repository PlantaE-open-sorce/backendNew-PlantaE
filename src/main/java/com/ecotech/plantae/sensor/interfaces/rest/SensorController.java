package com.ecotech.plantae.sensor.interfaces.rest;

import com.ecotech.plantae.sensor.application.internal.commands.DeactivateSensorCommand;
import com.ecotech.plantae.sensor.application.internal.commands.IngestSensorReadingCommand;
import com.ecotech.plantae.sensor.application.internal.commands.LinkSensorCommand;
import com.ecotech.plantae.sensor.application.internal.commands.RegisterSensorCommand;
import com.ecotech.plantae.sensor.application.internal.handlers.DeactivateSensorHandler;
import com.ecotech.plantae.sensor.application.internal.handlers.GetSensorActivityHandler;
import com.ecotech.plantae.sensor.application.internal.handlers.GetSensorByIdHandler;
import com.ecotech.plantae.sensor.application.internal.handlers.GetSensorReadingsHandler;
import com.ecotech.plantae.sensor.application.internal.handlers.IngestSensorReadingHandler;
import com.ecotech.plantae.sensor.application.internal.handlers.LinkSensorHandler;
import com.ecotech.plantae.sensor.application.internal.handlers.RegisterSensorHandler;
import com.ecotech.plantae.sensor.application.internal.handlers.SearchSensorsHandler;
import com.ecotech.plantae.sensor.application.internal.queries.GetSensorActivityQuery;
import com.ecotech.plantae.sensor.application.internal.queries.GetSensorByIdQuery;
import com.ecotech.plantae.sensor.application.internal.queries.GetSensorReadingsQuery;
import com.ecotech.plantae.sensor.application.internal.queries.SearchSensorsQuery;
import com.ecotech.plantae.sensor.domain.entities.Sensor;
import com.ecotech.plantae.sensor.domain.entities.SensorReading;
import com.ecotech.plantae.sensor.domain.models.PagedResult;
import com.ecotech.plantae.sensor.domain.models.SensorActivity;
import com.ecotech.plantae.shared.application.internal.security.CurrentUserProvider;
import com.ecotech.plantae.shared.interfaces.rest.MessageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sensors")
@Validated
@Tag(name = "BC: Sensor")
@SecurityRequirement(name = "bearerAuth")
public class SensorController {

    private final RegisterSensorHandler registerSensorHandler;
    private final LinkSensorHandler linkSensorHandler;
    private final DeactivateSensorHandler deactivateSensorHandler;
    private final SearchSensorsHandler searchSensorsHandler;
    private final GetSensorByIdHandler getSensorByIdHandler;
    private final IngestSensorReadingHandler ingestSensorReadingHandler;
    private final GetSensorReadingsHandler getSensorReadingsHandler;
    private final GetSensorActivityHandler getSensorActivityHandler;
    private final CurrentUserProvider currentUserProvider;

    public SensorController(RegisterSensorHandler registerSensorHandler,
            LinkSensorHandler linkSensorHandler,
            DeactivateSensorHandler deactivateSensorHandler,
            SearchSensorsHandler searchSensorsHandler,
            GetSensorByIdHandler getSensorByIdHandler,
            IngestSensorReadingHandler ingestSensorReadingHandler,
            GetSensorReadingsHandler getSensorReadingsHandler,
            GetSensorActivityHandler getSensorActivityHandler,
            CurrentUserProvider currentUserProvider) {
        this.registerSensorHandler = registerSensorHandler;
        this.linkSensorHandler = linkSensorHandler;
        this.deactivateSensorHandler = deactivateSensorHandler;
        this.searchSensorsHandler = searchSensorsHandler;
        this.getSensorByIdHandler = getSensorByIdHandler;
        this.ingestSensorReadingHandler = ingestSensorReadingHandler;
        this.getSensorReadingsHandler = getSensorReadingsHandler;
        this.getSensorActivityHandler = getSensorActivityHandler;
        this.currentUserProvider = currentUserProvider;
    }

    @PostMapping
    public ResponseEntity<Sensor> register(@Valid @RequestBody RegisterSensorRequest request) {
        Sensor sensor = registerSensorHandler.handle(new RegisterSensorCommand(
                request.type(),
                request.ownerId(),
                request.plantId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(sensor);
    }

    @PostMapping("/{sensorId}/link")
    public ResponseEntity<MessageResponse> link(@PathVariable String sensorId,
            @Valid @RequestBody LinkSensorRequest request) {
        try {
            linkSensorHandler.handle(new LinkSensorCommand(sensorId, request.plantId()));
            return ResponseEntity.accepted().body(new MessageResponse("Sensor linked"));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping("/{sensorId}/deactivate")
    public ResponseEntity<MessageResponse> deactivate(@PathVariable String sensorId) {
        try {
            deactivateSensorHandler.handle(new DeactivateSensorCommand(sensorId));
            return ResponseEntity.ok(new MessageResponse("Sensor deactivated"));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<PagedResult<Sensor>> list(@RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String plantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        String ownerId = currentUserProvider.getCurrentUserId()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
        PagedResult<Sensor> result = searchSensorsHandler
                .handle(new SearchSensorsQuery(ownerId, type, status, plantId, page, size));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{sensorId}")
    public ResponseEntity<Sensor> get(@PathVariable String sensorId) {
        Sensor sensor = getSensorByIdHandler.handle(new GetSensorByIdQuery(sensorId));
        if (sensor == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor not found");
        }
        return ResponseEntity.ok(sensor);
    }

    @PostMapping("/{sensorId}/readings")
    public ResponseEntity<MessageResponse> ingest(@PathVariable String sensorId,
            @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage,
            @Valid @RequestBody IngestReadingRequest request) {
        try {
            ingestSensorReadingHandler.handle(new IngestSensorReadingCommand(
                    sensorId,
                    request.timestamp(),
                    request.metric(),
                    request.value(),
                    request.quality(),
                    acceptLanguage));
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageResponse("Reading accepted"));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/{sensorId}/readings")
    public ResponseEntity<PagedResult<SensorReading>> getReadings(@PathVariable String sensorId,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) String metric,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PagedResult<SensorReading> result = getSensorReadingsHandler
                .handle(new GetSensorReadingsQuery(sensorId, from, to, metric, page, size));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/activity")
    public ResponseEntity<List<SensorActivity>> getActivity(@RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(defaultValue = "10") int top) {
        List<SensorActivity> result = getSensorActivityHandler.handle(new GetSensorActivityQuery(from, to, top));
        return ResponseEntity.ok(result);
    }

    public record RegisterSensorRequest(
            @NotBlank String type,
            @NotBlank String ownerId,
            @NotBlank String plantId) {
    }

    public record LinkSensorRequest(@NotBlank String plantId) {
    }

    public record IngestReadingRequest(
            String timestamp,
            @NotBlank String metric,
            @NotNull double value,
            @Size(max = 50) String quality) {
    }
}
