package com.ecotech.plantae.sensor.interfaces.rest;

import com.ecotech.plantae.sensor.application.commands.DeactivateSensorCommand;
import com.ecotech.plantae.sensor.application.commands.IngestSensorReadingCommand;
import com.ecotech.plantae.sensor.application.commands.LinkSensorCommand;
import com.ecotech.plantae.sensor.application.commands.RegisterSensorCommand;
import com.ecotech.plantae.sensor.application.handlers.DeactivateSensorHandler;
import com.ecotech.plantae.sensor.application.handlers.GetSensorActivityHandler;
import com.ecotech.plantae.sensor.application.handlers.GetSensorByIdHandler;
import com.ecotech.plantae.sensor.application.handlers.GetSensorReadingsHandler;
import com.ecotech.plantae.sensor.application.handlers.IngestSensorReadingHandler;
import com.ecotech.plantae.sensor.application.handlers.LinkSensorHandler;
import com.ecotech.plantae.sensor.application.handlers.RegisterSensorHandler;
import com.ecotech.plantae.sensor.application.handlers.SearchSensorsHandler;
import com.ecotech.plantae.sensor.application.queries.GetSensorActivityQuery;
import com.ecotech.plantae.sensor.application.queries.GetSensorByIdQuery;
import com.ecotech.plantae.sensor.application.queries.GetSensorReadingsQuery;
import com.ecotech.plantae.sensor.application.queries.SearchSensorsQuery;
import com.ecotech.plantae.sensor.domain.dtos.SensorActivityDto;
import com.ecotech.plantae.sensor.domain.dtos.SensorDto;
import com.ecotech.plantae.sensor.domain.dtos.SensorReadingDto;
import com.ecotech.plantae.sensor.domain.models.PagedResult;
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
import org.springframework.web.bind.annotation.PutMapping;
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

    public SensorController(RegisterSensorHandler registerSensorHandler,
                            LinkSensorHandler linkSensorHandler,
                            DeactivateSensorHandler deactivateSensorHandler,
                            SearchSensorsHandler searchSensorsHandler,
                            GetSensorByIdHandler getSensorByIdHandler,
                            IngestSensorReadingHandler ingestSensorReadingHandler,
                            GetSensorReadingsHandler getSensorReadingsHandler,
                            GetSensorActivityHandler getSensorActivityHandler) {
        this.registerSensorHandler = registerSensorHandler;
        this.linkSensorHandler = linkSensorHandler;
        this.deactivateSensorHandler = deactivateSensorHandler;
        this.searchSensorsHandler = searchSensorsHandler;
        this.getSensorByIdHandler = getSensorByIdHandler;
        this.ingestSensorReadingHandler = ingestSensorReadingHandler;
        this.getSensorReadingsHandler = getSensorReadingsHandler;
        this.getSensorActivityHandler = getSensorActivityHandler;
    }

    @PostMapping
    public ResponseEntity<SensorDto> register(@Valid @RequestBody RegisterSensorRequest request) {
        SensorDto dto = registerSensorHandler.handle(new RegisterSensorCommand(
                request.type(),
                request.ownerId(),
                request.plantId()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
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
    public ResponseEntity<PagedResult<SensorDto>> list(@RequestParam(required = false) String type,
                                                       @RequestParam(required = false) String status,
                                                       @RequestParam(required = false) String plantId,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "20") int size) {
        PagedResult<SensorDto> result = searchSensorsHandler.handle(new SearchSensorsQuery(type, status, plantId, page, size));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{sensorId}")
    public ResponseEntity<SensorDto> get(@PathVariable String sensorId) {
        SensorDto dto = getSensorByIdHandler.handle(new GetSensorByIdQuery(sensorId));
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor not found");
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{sensorId}/readings")
    public ResponseEntity<MessageResponse> ingest(@PathVariable String sensorId,
                                                  @Valid @RequestBody IngestReadingRequest request) {
        try {
            ingestSensorReadingHandler.handle(new IngestSensorReadingCommand(
                    sensorId,
                    request.timestamp(),
                    request.metric(),
                    request.value(),
                    request.quality()
            ));
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageResponse("Reading accepted"));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/{sensorId}/readings")
    public ResponseEntity<PagedResult<SensorReadingDto>> readings(@PathVariable String sensorId,
                                                                  @RequestParam(required = false) String from,
                                                                  @RequestParam(required = false) String to,
                                                                  @RequestParam(required = false) String metric,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "50") int size) {
        PagedResult<SensorReadingDto> result = getSensorReadingsHandler.handle(new GetSensorReadingsQuery(sensorId, from, to, metric, page, size));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/activity")
    public ResponseEntity<List<SensorActivityDto>> activity(@RequestParam(required = false) String from,
                                                            @RequestParam(required = false) String to,
                                                            @RequestParam(defaultValue = "10") int top) {
        List<SensorActivityDto> result = getSensorActivityHandler.handle(new GetSensorActivityQuery(from, to, top));
        return ResponseEntity.ok(result);
    }

    public record RegisterSensorRequest(
            @NotBlank String type,
            @NotBlank String ownerId,
            String plantId
    ) {}

    public record LinkSensorRequest(@NotBlank String plantId) {}

    public record IngestReadingRequest(
            String timestamp,
            @NotBlank String metric,
            @NotNull double value,
            @Size(max = 50) String quality
    ) {}
}
