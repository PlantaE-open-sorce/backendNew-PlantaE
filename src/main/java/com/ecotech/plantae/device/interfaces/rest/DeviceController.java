package com.ecotech.plantae.device.interfaces.rest;

import com.ecotech.plantae.device.application.commands.DeactivateDeviceCommand;
import com.ecotech.plantae.device.application.commands.LinkDeviceToPlantCommand;
import com.ecotech.plantae.device.application.commands.RegisterDeviceCommand;
import com.ecotech.plantae.device.application.commands.UpdateDeviceNoteCommand;
import com.ecotech.plantae.device.application.handlers.DeactivateDeviceHandler;
import com.ecotech.plantae.device.application.handlers.GetDeviceByIdHandler;
import com.ecotech.plantae.device.application.handlers.LinkDeviceToPlantHandler;
import com.ecotech.plantae.device.application.handlers.ListDevicesHandler;
import com.ecotech.plantae.device.application.handlers.RegisterDeviceHandler;
import com.ecotech.plantae.device.application.handlers.UpdateDeviceNoteHandler;
import com.ecotech.plantae.device.application.queries.GetDeviceByIdQuery;
import com.ecotech.plantae.device.application.queries.ListDevicesQuery;
import com.ecotech.plantae.device.domain.dtos.DeviceDto;
import com.ecotech.plantae.shared.application.security.CurrentUserProvider;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/devices")
@Validated
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "BC: Device")
public class DeviceController {

    private final RegisterDeviceHandler registerDeviceHandler;
    private final DeactivateDeviceHandler deactivateDeviceHandler;
    private final UpdateDeviceNoteHandler updateDeviceNoteHandler;
    private final GetDeviceByIdHandler getDeviceByIdHandler;
    private final LinkDeviceToPlantHandler linkDeviceToPlantHandler;
    private final ListDevicesHandler listDevicesHandler;
    private final CurrentUserProvider currentUserProvider;

    public DeviceController(RegisterDeviceHandler registerDeviceHandler,
                            DeactivateDeviceHandler deactivateDeviceHandler,
                            UpdateDeviceNoteHandler updateDeviceNoteHandler,
                            GetDeviceByIdHandler getDeviceByIdHandler,
                            LinkDeviceToPlantHandler linkDeviceToPlantHandler,
                            ListDevicesHandler listDevicesHandler,
                            CurrentUserProvider currentUserProvider) {
        this.registerDeviceHandler = registerDeviceHandler;
        this.deactivateDeviceHandler = deactivateDeviceHandler;
        this.updateDeviceNoteHandler = updateDeviceNoteHandler;
        this.getDeviceByIdHandler = getDeviceByIdHandler;
        this.linkDeviceToPlantHandler = linkDeviceToPlantHandler;
        this.listDevicesHandler = listDevicesHandler;
        this.currentUserProvider = currentUserProvider;
    }

    @PostMapping
    public ResponseEntity<DeviceDto> register(@Valid @RequestBody RegisterDeviceRequest request) {
        try {
            DeviceDto dto = registerDeviceHandler.handle(new RegisterDeviceCommand(
                    request.deviceId(), request.ownerId(), request.hwModel(), request.secret()
            ));
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        }
    }

    @PostMapping("/{deviceId}/deactivate")
    public ResponseEntity<DeviceDto> deactivate(@PathVariable String deviceId) {
        try {
            DeviceDto dto = deactivateDeviceHandler.handle(new DeactivateDeviceCommand(deviceId));
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PutMapping("/{deviceId}/note")
    public ResponseEntity<DeviceDto> updateNote(@PathVariable String deviceId,
                                                @Valid @RequestBody UpdateDeviceNoteRequest request) {
        try {
            DeviceDto dto = updateDeviceNoteHandler.handle(new UpdateDeviceNoteCommand(deviceId, request.note()));
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping("/{deviceId}/link")
    public ResponseEntity<Void> linkToPlant(@PathVariable String deviceId,
                                            @Valid @RequestBody LinkDeviceRequest request) {
        try {
            linkDeviceToPlantHandler.handle(new LinkDeviceToPlantCommand(deviceId, request.plantId()));
            return ResponseEntity.accepted().build();
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<DeviceDto> get(@PathVariable String deviceId) {
        DeviceDto dto = getDeviceByIdHandler.handle(new GetDeviceByIdQuery(deviceId));
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found");
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<DeviceDto>> list(@RequestParam(value = "activeOnly", defaultValue = "false") boolean activeOnly) {
        String ownerId = requireUser();
        List<DeviceDto> devices = listDevicesHandler.handle(new ListDevicesQuery(ownerId, activeOnly));
        return ResponseEntity.ok(devices);
    }

    private String requireUser() {
        return currentUserProvider.getCurrentUserId()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
    }

    public record RegisterDeviceRequest(
            @NotBlank String deviceId,
            @NotBlank String ownerId,
            @NotBlank @Size(max = 100) String hwModel,
            @Size(max = 255) String secret
    ) {}

    public record UpdateDeviceNoteRequest(
            @Size(max = 500) String note
    ) {}

    public record LinkDeviceRequest(
            @NotBlank String plantId
    ) {}
}
