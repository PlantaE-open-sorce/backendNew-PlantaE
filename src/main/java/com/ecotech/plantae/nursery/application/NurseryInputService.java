package com.ecotech.plantae.nursery.application;

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
import java.util.concurrent.ThreadLocalRandom;

@Service
public class NurseryInputService {

    private final Map<String, Deque<NurseryInputDto>> inputsByUser = new ConcurrentHashMap<>();

    public NurseryInputDto register(String userId, RegisterInputCommand command) {
        NurseryInputDto dto = new NurseryInputDto(
                UUID.randomUUID().toString(),
                command.assetId(),
                command.assetType(),
                command.inputType(),
                command.quantity(),
                command.unit(),
                command.cost(),
                resolveAppliedAt(command.appliedAt()),
                command.appliedBy());
        inputsByUser.computeIfAbsent(userId, id -> new ConcurrentLinkedDeque<>()).addFirst(dto);
        return dto;
    }

    public List<NurseryInputDto> listRecent(String userId) {
        Deque<NurseryInputDto> entries = inputsByUser.computeIfAbsent(userId, id -> new ConcurrentLinkedDeque<>());
        if (entries.isEmpty()) {
            entries.addAll(seedInputs());
        }
        return new ArrayList<>(entries);
    }

    private List<NurseryInputDto> seedInputs() {
        List<NurseryInputDto> seeded = new ArrayList<>();
        seeded.add(new NurseryInputDto(
                UUID.randomUUID().toString(),
                "batch-1",
                "BATCH",
                "FERTILIZER",
                25,
                "kg",
                120.5,
                resolveAppliedAt(null),
                "Sistema"));
        seeded.add(new NurseryInputDto(
                UUID.randomUUID().toString(),
                "batch-2",
                "BATCH",
                "PESTICIDE",
                8,
                "L",
                90.0,
                resolveAppliedAt(null),
                "Sistema"));
        return seeded;
    }

    private String resolveAppliedAt(String appliedAt) {
        if (appliedAt != null && !appliedAt.isBlank()) {
            return appliedAt;
        }
        Instant defaultDate = Instant.now().minusSeconds(ThreadLocalRandom.current().nextLong(2, 48) * 3600);
        return DateTimeFormatter.ISO_INSTANT.format(defaultDate);
    }

    public record RegisterInputCommand(
            String assetId,
            String assetType,
            String inputType,
            double quantity,
            String unit,
            double cost,
            String appliedAt,
            String appliedBy) {
    }

    public record NurseryInputDto(
            String id,
            String assetId,
            String assetType,
            String inputType,
            double quantity,
            String unit,
            double cost,
            String appliedAt,
            String appliedBy) {
    }
}
