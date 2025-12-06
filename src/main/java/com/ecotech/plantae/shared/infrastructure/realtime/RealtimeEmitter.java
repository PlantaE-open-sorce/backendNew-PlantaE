package com.ecotech.plantae.shared.infrastructure.realtime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class RealtimeEmitter {

    private static final Logger log = LoggerFactory.getLogger(RealtimeEmitter.class);
    private static final long SSE_TIMEOUT_MS = 30 * 60 * 1000L; // 30 minutes

    private final Map<String, List<SseEmitter>> readingEmitters = new ConcurrentHashMap<>();
    private final Map<String, List<SseEmitter>> alertEmitters = new ConcurrentHashMap<>();

    public SseEmitter subscribeToReadings(String ownerId) {
        return register(ownerId, readingEmitters);
    }

    public SseEmitter subscribeToAlerts(String ownerId) {
        return register(ownerId, alertEmitters);
    }

    public void publishReading(String ownerId, Object payload) {
        publish(ownerId, readingEmitters, "reading", payload);
    }

    public void publishAlert(String ownerId, Object payload) {
        publish(ownerId, alertEmitters, "alert", payload);
    }

    private SseEmitter register(String ownerId, Map<String, List<SseEmitter>> store) {
        Objects.requireNonNull(ownerId, "ownerId is required");
        // Use timeout to prevent resource leaks when client disconnects abruptly
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT_MS);
        store.computeIfAbsent(ownerId, key -> new CopyOnWriteArrayList<>()).add(emitter);

        Runnable cleanup = () -> {
            try {
                removeEmitter(ownerId, emitter, store);
            } catch (Exception e) {
                log.debug("Error during emitter cleanup for {}: {}", ownerId, e.getMessage());
            }
        };

        emitter.onCompletion(cleanup);
        emitter.onTimeout(() -> {
            log.debug("SSE timeout for owner: {}", ownerId);
            cleanup.run();
        });
        emitter.onError(error -> {
            log.debug("SSE error for owner {}: {}", ownerId, error.getMessage());
            cleanup.run();
        });

        // Initial comment to confirm connection
        try {
            emitter.send(SseEmitter.event()
                    .name("init")
                    .data("connected", MediaType.TEXT_PLAIN));
        } catch (IOException ex) {
            log.debug("Failed to send init event: {}", ex.getMessage());
            cleanup.run();
        }
        return emitter;
    }

    private void publish(String ownerId,
            Map<String, List<SseEmitter>> store,
            String eventName,
            Object payload) {
        List<SseEmitter> emitters = store.get(ownerId);
        if (emitters == null || emitters.isEmpty()) {
            return;
        }
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name(Objects.requireNonNull(eventName))
                        .data(Objects.requireNonNull(payload)));
            } catch (IOException ex) {
                log.debug("Removing emitter for {} after failure: {}", ownerId, ex.getMessage());
                emitter.completeWithError(ex);
                removeEmitter(ownerId, emitter, store);
            }
        });
    }

    private void removeEmitter(String ownerId, SseEmitter emitter, Map<String, List<SseEmitter>> store) {
        List<SseEmitter> emitters = store.get(ownerId);
        if (emitters == null) {
            return;
        }
        emitters.remove(emitter);
        if (emitters.isEmpty()) {
            store.remove(ownerId);
        }
    }
}
