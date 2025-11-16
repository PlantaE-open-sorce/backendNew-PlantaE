package com.ecotech.plantae.sensor.infrastructure.simulator;

import com.ecotech.plantae.sensor.application.commands.IngestSensorReadingCommand;
import com.ecotech.plantae.sensor.application.handlers.IngestSensorReadingHandler;
import com.ecotech.plantae.sensor.domain.models.SensorSearchCriteria;
import com.ecotech.plantae.sensor.domain.repositories.SensorReadingRepository;
import com.ecotech.plantae.sensor.domain.repositories.SensorRepository;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorMetric;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorType;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorStatus;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Random;

@Component
@EnableScheduling
public class SensorDataGenerator {

    private final SensorRepository sensorRepository;
    private final IngestSensorReadingHandler ingestSensorReadingHandler;
    private final Random random = new Random();

    public SensorDataGenerator(SensorRepository sensorRepository,
                               SensorReadingRepository sensorReadingRepository) {
        this.sensorRepository = sensorRepository;
        this.ingestSensorReadingHandler = new IngestSensorReadingHandler(sensorRepository, sensorReadingRepository);
    }

    @PostConstruct
    public void warmup() {
        simulateReadings();
    }

    @Scheduled(fixedDelayString = "${simulation.sensors.delay-ms:15000}")
    public void simulateReadings() {
        List<com.ecotech.plantae.sensor.domain.entities.Sensor> sensors = sensorRepository.search(
                new SensorSearchCriteria(null, SensorStatus.ACTIVE, null, 0, 100)).content();
        sensors.forEach(sensor -> {
            List<SensorMetric> metrics = metricsFor(sensor.getType());
            metrics.forEach(metric -> ingestSensorReadingHandler.handle(new IngestSensorReadingCommand(
                    sensor.getId().value(),
                    Instant.now().toString(),
                    metric.apiName(),
                    randomValue(metric),
                    "AUTO"
            )));
        });
    }

    private List<SensorMetric> metricsFor(SensorType type) {
        return switch (type) {
            case SOIL -> List.of(SensorMetric.SOILMOISTURE);
            case TEMPERATURE -> List.of(SensorMetric.TEMPERATURE);
            case HUMIDITY -> List.of(SensorMetric.HUMIDITY);
            case LIGHT -> List.of(SensorMetric.LIGHT);
            case MULTI -> List.of(
                    SensorMetric.SOILMOISTURE,
                    SensorMetric.TEMPERATURE,
                    SensorMetric.HUMIDITY,
                    SensorMetric.LIGHT
            );
        };
    }

    private double randomValue(SensorMetric metric) {
        return switch (metric) {
            case SOILMOISTURE -> 20 + random.nextDouble() * 40;
            case TEMPERATURE -> 15 + random.nextDouble() * 15;
            case HUMIDITY -> 40 + random.nextDouble() * 50;
            case LIGHT -> 200 + random.nextDouble() * 600;
        };
    }
}
