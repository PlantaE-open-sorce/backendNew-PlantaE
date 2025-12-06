package com.ecotech.plantae.shared.interfaces.rest;

import com.ecotech.plantae.shared.application.internal.security.CurrentUserProvider;
import com.ecotech.plantae.shared.infrastructure.realtime.RealtimeEmitter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/stream")
@Tag(name = "Shared: Stream")
@SecurityRequirement(name = "bearerAuth")
public class StreamController {

    private final RealtimeEmitter emitter;
    private final CurrentUserProvider currentUserProvider;

    public StreamController(RealtimeEmitter emitter, CurrentUserProvider currentUserProvider) {
        this.emitter = emitter;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping("/readings")
    public SseEmitter readings() {
        return emitter.subscribeToReadings(requireUser());
    }

    @GetMapping("/alerts")
    public SseEmitter alerts() {
        return emitter.subscribeToAlerts(requireUser());
    }

    private String requireUser() {
        return currentUserProvider.getCurrentUserId()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
    }
}
