package com.ecotech.plantae.plant.infrastructure.config;

import com.ecotech.plantae.plant.application.handlers.CreatePlantHandler;
import com.ecotech.plantae.plant.application.handlers.DeletePlantHandler;
import com.ecotech.plantae.plant.application.handlers.GetPlantByIdHandler;
import com.ecotech.plantae.plant.application.handlers.SearchPlantsHandler;
import com.ecotech.plantae.plant.application.handlers.UpdatePlantHandler;
import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlantBeansConfig {

    @Bean
    public CreatePlantHandler createPlantHandler(PlantRepository plantRepository) {
        return new CreatePlantHandler(plantRepository);
    }

    @Bean
    public GetPlantByIdHandler getPlantByIdHandler(PlantRepository plantRepository) {
        return new GetPlantByIdHandler(plantRepository);
    }

    @Bean
    public UpdatePlantHandler updatePlantHandler(PlantRepository plantRepository) {
        return new UpdatePlantHandler(plantRepository);
    }

    @Bean
    public DeletePlantHandler deletePlantHandler(PlantRepository plantRepository) {
        return new DeletePlantHandler(plantRepository);
    }

    @Bean
    public SearchPlantsHandler searchPlantsHandler(PlantRepository plantRepository) {
        return new SearchPlantsHandler(plantRepository);
    }
}
