package com.ecotech.plantae.plant.infrastructure.config;

import com.ecotech.plantae.plant.application.internal.handlers.CreatePlantHandler;
import com.ecotech.plantae.plant.application.internal.handlers.DeletePlantHandler;
import com.ecotech.plantae.plant.application.internal.handlers.GetPlantByIdHandler;
import com.ecotech.plantae.plant.application.internal.handlers.SearchPlantsHandler;
import com.ecotech.plantae.plant.application.internal.handlers.UpdatePlantHandler;
import com.ecotech.plantae.plant.application.internal.services.SpeciesCatalogService;
import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlantBeansConfig {

    @Bean
    public CreatePlantHandler createPlantHandler(PlantRepository plantRepository, SpeciesCatalogService speciesCatalogService) {
        return new CreatePlantHandler(plantRepository, speciesCatalogService);
    }

    @Bean
    public GetPlantByIdHandler getPlantByIdHandler(PlantRepository plantRepository) {
        return new GetPlantByIdHandler(plantRepository);
    }

    @Bean
    public UpdatePlantHandler updatePlantHandler(PlantRepository plantRepository, SpeciesCatalogService speciesCatalogService) {
        return new UpdatePlantHandler(plantRepository, speciesCatalogService);
    }

    @Bean
    public DeletePlantHandler deletePlantHandler(PlantRepository plantRepository) {
        return new DeletePlantHandler(plantRepository);
    }

    @Bean
    public SearchPlantsHandler searchPlantsHandler(PlantRepository plantRepository) {
        return new SearchPlantsHandler(plantRepository);
    }

    @Bean
    public SpeciesCatalogService speciesCatalogService() {
        return new SpeciesCatalogService();
    }
}
