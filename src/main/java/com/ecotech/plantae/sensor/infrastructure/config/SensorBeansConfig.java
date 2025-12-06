package com.ecotech.plantae.sensor.infrastructure.config;

import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import com.ecotech.plantae.sensor.application.internal.handlers.DeactivateSensorHandler;
import com.ecotech.plantae.sensor.application.internal.handlers.GetSensorActivityHandler;
import com.ecotech.plantae.sensor.application.internal.handlers.GetSensorByIdHandler;
import com.ecotech.plantae.sensor.application.internal.handlers.GetSensorReadingsHandler;
import com.ecotech.plantae.sensor.application.internal.handlers.IngestSensorReadingHandler;
import com.ecotech.plantae.sensor.application.internal.handlers.LinkSensorHandler;
import com.ecotech.plantae.sensor.application.internal.handlers.RegisterSensorHandler;
import com.ecotech.plantae.sensor.application.internal.handlers.SearchSensorsHandler;
import com.ecotech.plantae.sensor.domain.repositories.SensorReadingRepository;
import com.ecotech.plantae.sensor.domain.repositories.SensorRepository;
import com.ecotech.plantae.shared.infrastructure.realtime.RealtimeEmitter;
import com.ecotech.plantae.alert.application.internal.handlers.RaiseAlertHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SensorBeansConfig {

    @Bean
    public RegisterSensorHandler registerSensorHandler(SensorRepository sensorRepository, PlantRepository plantRepository) {
        return new RegisterSensorHandler(sensorRepository, plantRepository);
    }

    @Bean
    public LinkSensorHandler linkSensorHandler(SensorRepository sensorRepository, PlantRepository plantRepository) {
        return new LinkSensorHandler(sensorRepository, plantRepository);
    }

    @Bean
    public DeactivateSensorHandler deactivateSensorHandler(SensorRepository sensorRepository) {
        return new DeactivateSensorHandler(sensorRepository);
    }

    @Bean
    public SearchSensorsHandler searchSensorsHandler(SensorRepository sensorRepository) {
        return new SearchSensorsHandler(sensorRepository);
    }

    @Bean
    public GetSensorByIdHandler getSensorByIdHandler(SensorRepository sensorRepository) {
        return new GetSensorByIdHandler(sensorRepository);
    }

    @Bean
    public IngestSensorReadingHandler ingestSensorReadingHandler(SensorRepository sensorRepository,
                                                                 SensorReadingRepository sensorReadingRepository,
                                                                 RealtimeEmitter realtimeEmitter,
                                                                 RaiseAlertHandler raiseAlertHandler) {
        return new IngestSensorReadingHandler(sensorRepository, sensorReadingRepository, realtimeEmitter, raiseAlertHandler);
    }

    @Bean
    public GetSensorReadingsHandler getSensorReadingsHandler(SensorReadingRepository sensorReadingRepository) {
        return new GetSensorReadingsHandler(sensorReadingRepository);
    }

    @Bean
    public GetSensorActivityHandler getSensorActivityHandler(SensorReadingRepository sensorReadingRepository) {
        return new GetSensorActivityHandler(sensorReadingRepository);
    }
}
