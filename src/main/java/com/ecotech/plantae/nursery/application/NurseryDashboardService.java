package com.ecotech.plantae.nursery.application;

import com.ecotech.plantae.nursery.domain.dto.NurseryBatchCard;
import com.ecotech.plantae.nursery.domain.dto.NurseryDashboardResponse;
import com.ecotech.plantae.nursery.domain.dto.NurseryInputDto;
import com.ecotech.plantae.nursery.domain.dto.NurseryTaskDto;
import com.ecotech.plantae.plant.application.handlers.SearchPlantsHandler;
import com.ecotech.plantae.plant.application.queries.SearchPlantsQuery;
import com.ecotech.plantae.plant.domain.dtos.PlantDto;
import com.ecotech.plantae.shared.application.simulation.HardwareProfileGenerator;
import com.ecotech.plantae.shared.application.simulation.HardwareProfileGenerator.HardwareSnapshot;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class NurseryDashboardService {

    private final SearchPlantsHandler searchPlantsHandler;
    private final HardwareProfileGenerator hardwareProfileGenerator;
    private final NurseryTaskService nurseryTaskService;
    private final NurseryInputService nurseryInputService;

    public NurseryDashboardService(SearchPlantsHandler searchPlantsHandler,
                                   HardwareProfileGenerator hardwareProfileGenerator,
                                   NurseryTaskService nurseryTaskService,
                                   NurseryInputService nurseryInputService) {
        this.searchPlantsHandler = searchPlantsHandler;
        this.hardwareProfileGenerator = hardwareProfileGenerator;
        this.nurseryTaskService = nurseryTaskService;
        this.nurseryInputService = nurseryInputService;
    }

    public NurseryDashboardResponse build(String userId) {
        List<NurseryBatchCard> batches = summarizeBatches(userId);
        List<NurseryTaskDto> tasks = nurseryTaskService.listTodos(userId);
        List<NurseryInputDto> inputs = nurseryInputService.listRecent(userId);
        List<HardwareSnapshot> hardware = hardwareProfileGenerator.generateMany(Math.max(1, batches.size()));

        return new NurseryDashboardResponse(
                countPlants(userId),
                batches.size(),
                tasks.size(),
                batches.stream().limit(4).toList(),
                tasks.stream().limit(5).toList(),
                inputs.stream().limit(5).toList(),
                hardware
        );
    }

    public List<NurseryBatchCard> batches(String userId) {
        return summarizeBatches(userId);
    }

    private int countPlants(String ownerId) {
        return searchPlantsHandler.handle(new SearchPlantsQuery(
                ownerId,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "createdAt,desc",
                0,
                100
        )).content().size();
    }

    private List<NurseryBatchCard> summarizeBatches(String ownerId) {
        List<PlantDto> plants = searchPlantsHandler.handle(new SearchPlantsQuery(
                ownerId,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "species,asc",
                0,
                100
        )).content();

        if (plants.isEmpty()) {
            return List.of();
        }

        Map<String, List<PlantDto>> bySpecies = plants.stream()
                .collect(Collectors.groupingBy(dto -> dto.species() != null ? dto.species() : "Unknown"));

        List<NurseryBatchCard> cards = new ArrayList<>();
        bySpecies.forEach((species, speciesPlants) -> {
            String batchId = "batch-" + species.toLowerCase().replace(" ", "-");
            cards.add(new NurseryBatchCard(
                    batchId,
                    "Lote " + species,
                    species,
                    speciesPlants.size(),
                    pickRoutineStatus(),
                    ThreadLocalRandom.current().nextInt(45, 100),
                    hardwareProfileGenerator.generate(batchId)
            ));
        });

        cards.sort(Comparator.comparingInt(NurseryBatchCard::plantCount).reversed());
        return cards;
    }

    private String pickRoutineStatus() {
        return switch (ThreadLocalRandom.current().nextInt(3)) {
            case 0 -> "ON_TRACK";
            case 1 -> "DELAYED";
            default -> "AT_RISK";
        };
    }
}
