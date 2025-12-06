package com.ecotech.plantae.home.application;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
public class HomeManualActionService {

    private final Map<String, Deque<HomeManualActionDto>> actionsByUser = new ConcurrentHashMap<>();

    public HomeManualActionDto register(String userId, RegisterManualActionCommand command) {
        Instant performedAt = command.performedAt() != null ? command.performedAt() : Instant.now();
        HomeManualActionDto dto = new HomeManualActionDto(
                UUID.randomUUID().toString(),
                command.actionType(),
                command.notes(),
                DateTimeFormatter.ISO_INSTANT.format(performedAt),
                command.durationMinutes());
        actionsByUser.computeIfAbsent(userId, id -> new ConcurrentLinkedDeque<>()).addFirst(dto);
        return dto;
    }

    public List<HomeManualActionDto> list(String userId) {
        return new ArrayList<>(actionsByUser.getOrDefault(userId, new ConcurrentLinkedDeque<>()));
    }

    public record RegisterManualActionCommand(
            String actionType,
            String notes,
            Instant performedAt,
            int durationMinutes) {
    }

    public record HomeManualActionDto(
            String id,
            String actionType,
            String notes,
            String performedAt,
            int durationMinutes) {
    }
}
