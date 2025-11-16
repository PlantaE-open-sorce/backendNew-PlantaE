package com.ecotech.plantae.home.application;

import com.ecotech.plantae.home.domain.dto.HomeDashboardResponse;
import com.ecotech.plantae.home.domain.dto.HomeManualActionDto;
import com.ecotech.plantae.home.domain.dto.HomePlantCard;
import com.ecotech.plantae.plant.application.handlers.SearchPlantsHandler;
import com.ecotech.plantae.plant.application.queries.SearchPlantsQuery;
import com.ecotech.plantae.plant.domain.dtos.PlantDto;
import com.ecotech.plantae.shared.application.simulation.HardwareProfileGenerator;
import com.ecotech.plantae.shared.application.simulation.HardwareProfileGenerator.HardwareSnapshot;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class HomeDashboardService {

    private static final List<String> TIPS = List.of(
            "Rota tus macetas para un crecimiento uniforme.",
            "Verifica el drenaje antes de volver a regar.",
            "Limpia las hojas para mejorar la fotosíntesis."
    );

    private final SearchPlantsHandler searchPlantsHandler;
    private final HomeManualActionService manualActionService;
    private final HardwareProfileGenerator hardwareProfileGenerator;

    public HomeDashboardService(SearchPlantsHandler searchPlantsHandler,
                                HomeManualActionService manualActionService,
                                HardwareProfileGenerator hardwareProfileGenerator) {
        this.searchPlantsHandler = searchPlantsHandler;
        this.manualActionService = manualActionService;
        this.hardwareProfileGenerator = hardwareProfileGenerator;
    }

    public HomeDashboardResponse build(String userId) {
        List<HomePlantCard> plants = fetchPlantCards(userId);
        List<HomeManualActionDto> recentActions = manualActionService.list(userId).stream()
                .limit(3)
                .toList();
        List<HardwareSnapshot> hardware = plants.stream()
                .limit(3)
                .map(card -> hardwareProfileGenerator.generate(card.id()))
                .toList();

        String greeting = plants.isEmpty()
                ? "Aún no tienes plantas registradas"
                : "Tienes " + plants.size() + " plantas activas listas para cuidar";

        return new HomeDashboardResponse(
                greeting,
                randomTip(),
                plants,
                recentActions,
                hardware
        );
    }

    private List<HomePlantCard> fetchPlantCards(String ownerId) {
        return searchPlantsHandler.handle(new SearchPlantsQuery(
                ownerId,
                null,
                null,
                "ACTIVE",
                null,
                null,
                null,
                null,
                "createdAt,desc",
                0,
                5
        )).content().stream()
                .map(this::toHomePlantCard)
                .toList();
    }

    private HomePlantCard toHomePlantCard(PlantDto dto) {
        return new HomePlantCard(
                dto.id(),
                dto.name(),
                dto.species(),
                dto.status(),
                dto.hasAlerts()
        );
    }

    private String randomTip() {
        return TIPS.get(ThreadLocalRandom.current().nextInt(TIPS.size()));
    }
}
