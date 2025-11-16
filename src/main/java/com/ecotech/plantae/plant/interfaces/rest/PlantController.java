package com.ecotech.plantae.plant.interfaces.rest;

import com.ecotech.plantae.alert.application.handlers.GetPlantAlertsHandler;
import com.ecotech.plantae.alert.application.queries.GetPlantAlertsQuery;
import com.ecotech.plantae.alert.domain.dtos.AlertDto;
import com.ecotech.plantae.plant.application.commands.CreatePlantCommand;
import com.ecotech.plantae.plant.application.commands.DeletePlantCommand;
import com.ecotech.plantae.plant.application.commands.UpdatePlantCommand;
import com.ecotech.plantae.plant.application.handlers.CreatePlantHandler;
import com.ecotech.plantae.plant.application.handlers.DeletePlantHandler;
import com.ecotech.plantae.plant.application.handlers.GetPlantByIdHandler;
import com.ecotech.plantae.plant.application.handlers.SearchPlantsHandler;
import com.ecotech.plantae.plant.application.handlers.UpdatePlantHandler;
import com.ecotech.plantae.plant.application.queries.GetPlantByIdQuery;
import com.ecotech.plantae.plant.application.queries.SearchPlantsQuery;
import com.ecotech.plantae.plant.domain.dtos.PlantDto;
import com.ecotech.plantae.plant.domain.repositories.models.PagedResult;
import com.ecotech.plantae.shared.application.i18n.LanguageResolver;
import com.ecotech.plantae.shared.application.i18n.LocalizedMessageService;
import com.ecotech.plantae.shared.application.security.CurrentUserProvider;
import com.ecotech.plantae.shared.interfaces.rest.MessageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/v1/plants")
@Validated
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "BC: Plant")
public class PlantController {

    private final CreatePlantHandler createPlantHandler;
    private final GetPlantByIdHandler getPlantByIdHandler;
    private final UpdatePlantHandler updatePlantHandler;
    private final DeletePlantHandler deletePlantHandler;
    private final SearchPlantsHandler searchPlantsHandler;
    private final GetPlantAlertsHandler getPlantAlertsHandler;
    private final LanguageResolver languageResolver;
    private final LocalizedMessageService localizedMessageService;
    private final CurrentUserProvider currentUserProvider;

    public PlantController(CreatePlantHandler createPlantHandler,
                           GetPlantByIdHandler getPlantByIdHandler,
                           UpdatePlantHandler updatePlantHandler,
                           DeletePlantHandler deletePlantHandler,
                           SearchPlantsHandler searchPlantsHandler,
                           GetPlantAlertsHandler getPlantAlertsHandler,
                           LanguageResolver languageResolver,
                           LocalizedMessageService localizedMessageService,
                           CurrentUserProvider currentUserProvider) {
        this.createPlantHandler = createPlantHandler;
        this.getPlantByIdHandler = getPlantByIdHandler;
        this.updatePlantHandler = updatePlantHandler;
        this.deletePlantHandler = deletePlantHandler;
        this.searchPlantsHandler = searchPlantsHandler;
        this.getPlantAlertsHandler = getPlantAlertsHandler;
        this.languageResolver = languageResolver;
        this.localizedMessageService = localizedMessageService;
        this.currentUserProvider = currentUserProvider;
    }

    @PostMapping
    public ResponseEntity<PlantDto> create(@Valid @RequestBody CreatePlantRequest request) {
        String ownerId = requireUser();
        PlantDto dto = createPlantHandler.handle(new CreatePlantCommand(
                ownerId,
                request.name(),
                request.species(),
                request.deviceId(),
                request.sensorId()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public ResponseEntity<PagedResult<PlantDto>> search(@RequestParam(required = false) String name,
                                                        @RequestParam(required = false) String species,
                                                        @RequestParam(required = false) String status,
                                                        @RequestParam(required = false) String createdFrom,
                                                        @RequestParam(required = false) String createdTo,
                                                        @RequestParam(required = false) Boolean hasAlerts,
                                                        @RequestParam(required = false) String sensorId,
                                                        @RequestParam(required = false, defaultValue = "createdAt,desc") String sort,
                                                        @RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "20") int size) {
        String ownerId = requireUser();
        PagedResult<PlantDto> result = searchPlantsHandler.handle(new SearchPlantsQuery(
                ownerId,
                name,
                species,
                status,
                createdFrom,
                createdTo,
                hasAlerts,
                sensorId,
                sort,
                page,
                size
        ));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlantDto> get(@PathVariable String id) {
        String ownerId = requireUser();
        PlantDto dto = getPlantByIdHandler.handle(new GetPlantByIdQuery(ownerId, id));
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plant not found");
        }
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> update(@RequestHeader(value = "Accept-Language", required = false) String acceptLanguage,
                                                  @PathVariable String id,
                                                  @Valid @RequestBody UpdatePlantRequest request) {
        try {
            String ownerId = requireUser();
            updatePlantHandler.handle(new UpdatePlantCommand(
                    ownerId,
                    id,
                    request.name(),
                    request.species(),
                    request.status(),
                    request.deviceId(),
                    request.sensorId()
            ));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        String message = localizedMessageService.getMessage(
                "plant.update.success",
                languageResolver.resolve(acceptLanguage, "en")
        );
        return ResponseEntity.ok(new MessageResponse(message));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> delete(@RequestHeader(value = "Accept-Language", required = false) String acceptLanguage,
                                                  @PathVariable String id) {
        String ownerId = requireUser();
        try {
            deletePlantHandler.handle(new DeletePlantCommand(ownerId, id));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        String message = localizedMessageService.getMessage(
                "plant.delete.success",
                languageResolver.resolve(acceptLanguage, "en")
        );
        return ResponseEntity.ok(new MessageResponse(message));
    }

    @GetMapping("/{id}/alerts")
    public ResponseEntity<List<AlertDto>> plantAlerts(@RequestHeader(value = "Accept-Language", required = false) String acceptLanguage,
                                                      @PathVariable String id,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "20") int size) {
        String ownerId = currentUserProvider.getCurrentUserId()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
        List<AlertDto> alerts = getPlantAlertsHandler.handle(new GetPlantAlertsQuery(ownerId, id, page, size, acceptLanguage));
        return ResponseEntity.ok(alerts);
    }

    public record CreatePlantRequest(
            @NotBlank @Size(max = 100) String name,
            @NotBlank @Size(max = 100) String species,
            @Size(max = 100) String deviceId,
            @Size(max = 100) String sensorId
    ) {}

    public record UpdatePlantRequest(
            @Size(max = 100) String name,
            @Size(max = 100) String species,
            String status,
            @Size(max = 100) String deviceId,
            @Size(max = 100) String sensorId
    ) {}

    private String requireUser() {
        return currentUserProvider.getCurrentUserId()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
    }
}
