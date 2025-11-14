package com.ecotech.plantae.sensor.infrastructure.config;

import com.ecotech.plantae.sensor.domain.repositories.SensorRepository;
import com.ecotech.plantae.sensor.domain.repositories.SensorReadingRepository;
import com.ecotech.plantae.sensor.infrastructure.simulator.SensorDataGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SensorSimulationConfig {

    @Bean
    @ConditionalOnProperty(name = "simulation.sensors.enabled", havingValue = "true", matchIfMissing = true)
    public SensorDataGenerator sensorDataGenerator(SensorRepository sensorRepository,
                                                   SensorReadingRepository sensorReadingRepository) {
        return new SensorDataGenerator(sensorRepository, sensorReadingRepository);
    }
}
