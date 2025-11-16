package com.ecotech.plantae.alert.interfaces.rest;

import com.ecotech.plantae.alert.application.handlers.GetRecentAlertsHandler;
import com.ecotech.plantae.alert.application.queries.GetRecentAlertsQuery;
import com.ecotech.plantae.alert.application.commands.ResolveAlertCommand;
import com.ecotech.plantae.alert.application.handlers.ResolveAlertHandler;
import com.ecotech.plantae.alert.domain.dtos.AlertDto;
import com.ecotech.plantae.shared.application.security.CurrentUserProvider;
import com.ecotech.plantae.shared.interfaces.rest.MessageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/alerts")
@Validated
@Tag(name = "BC: Alert")
@SecurityRequirement(name = "bearerAuth")
public class AlertController {

    private final GetRecentAlertsHandler getRecentAlertsHandler;
    private final ResolveAlertHandler resolveAlertHandler;
    private final CurrentUserProvider currentUserProvider;

    public AlertController(GetRecentAlertsHandler getRecentAlertsHandler,
                           ResolveAlertHandler resolveAlertHandler,
                           CurrentUserProvider currentUserProvider) {
        this.getRecentAlertsHandler = getRecentAlertsHandler;
        this.resolveAlertHandler = resolveAlertHandler;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping("/recent")
    public ResponseEntity<List<AlertDto>> recent(@RequestHeader(value = "Accept-Language", required = false) String acceptLanguage,
                                                 @RequestParam(required = false) String plantId,
                                                 @RequestParam(required = false) String sensorId,
                                                 @RequestParam(required = false) String type,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "20") int size) {
        String ownerId = requireUser();
        List<AlertDto> alerts = getRecentAlertsHandler.handle(new GetRecentAlertsQuery(ownerId, plantId, sensorId, type, page, size, acceptLanguage));
        return ResponseEntity.ok(alerts);
    }

    @PostMapping("/{alertId}/resolve")
    public ResponseEntity<MessageResponse> resolve(@PathVariable String alertId) {
        requireUser();
        try {
            resolveAlertHandler.handle(new ResolveAlertCommand(alertId));
            return ResponseEntity.ok(new MessageResponse("Alert resolved"));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    private String requireUser() {
        return currentUserProvider.getCurrentUserId()
                .orElseThrow(() -> new ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Unauthorized"));
    }
}
