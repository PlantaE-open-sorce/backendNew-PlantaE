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
public class NurseryTaskService {

    private final Map<String, Deque<NurseryTaskDto>> tasksByUser = new ConcurrentHashMap<>();

    public NurseryTaskDto schedule(String userId, ScheduleSpecificTaskCommand command) {
        NurseryTaskDto dto = new NurseryTaskDto(
                UUID.randomUUID().toString(),
                command.title(),
                command.assetId(),
                command.assetType(),
                resolveDueDate(command.dueDate()),
                command.priority(),
                "PENDING",
                command.notes());
        tasksByUser.computeIfAbsent(userId, id -> new ConcurrentLinkedDeque<>()).addFirst(dto);
        return dto;
    }

    public List<NurseryTaskDto> listTodos(String userId) {
        Deque<NurseryTaskDto> tasks = tasksByUser.computeIfAbsent(userId, id -> new ConcurrentLinkedDeque<>());
        if (tasks.isEmpty()) {
            tasks.addAll(seedTasks());
        }
        return new ArrayList<>(tasks);
    }

    private List<NurseryTaskDto> seedTasks() {
        List<NurseryTaskDto> seeded = new ArrayList<>();
        String[] titles = { "Nutrir lote A", "Inspeccionar riego", "Control de plagas" };
        for (String title : titles) {
            seeded.add(new NurseryTaskDto(
                    UUID.randomUUID().toString(),
                    title,
                    "batch-" + ThreadLocalRandom.current().nextInt(1, 4),
                    "BATCH",
                    resolveDueDate(null),
                    pickPriority(),
                    "PENDING",
                    "Tarea sugerida por el sistema"));
        }
        return seeded;
    }

    private String resolveDueDate(String dueDate) {
        if (dueDate != null && !dueDate.isBlank()) {
            return dueDate;
        }
        Instant defaultDue = Instant.now().plusSeconds(ThreadLocalRandom.current().nextLong(6, 72) * 3600);
        return DateTimeFormatter.ISO_INSTANT.format(defaultDue);
    }

    private String pickPriority() {
        return ThreadLocalRandom.current().nextBoolean() ? "HIGH" : "MEDIUM";
    }

    public record ScheduleSpecificTaskCommand(
            String title,
            String assetId,
            String assetType,
            String dueDate,
            String priority,
            String notes) {
    }

    public record NurseryTaskDto(
            String id,
            String title,
            String assetId,
            String assetType,
            String dueDate,
            String priority,
            String status,
            String notes) {
    }
}
