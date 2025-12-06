package com.ecotech.plantae.sensor.infrastructure.simulator;

import com.ecotech.plantae.sensor.application.internal.commands.IngestSensorReadingCommand;
import com.ecotech.plantae.sensor.application.internal.handlers.IngestSensorReadingHandler;
import com.ecotech.plantae.sensor.domain.models.SensorSearchCriteria;
import com.ecotech.plantae.sensor.domain.repositories.SensorRepository;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorMetric;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorType;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorStatus;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

@Component
@EnableScheduling
public class SensorDataGenerator {

    private final SensorRepository sensorRepository;
    private final IngestSensorReadingHandler ingestSensorReadingHandler;
    private final Random random = new Random();

    public SensorDataGenerator(SensorRepository sensorRepository,
            IngestSensorReadingHandler ingestSensorReadingHandler) {
        this.sensorRepository = sensorRepository;
        this.ingestSensorReadingHandler = ingestSensorReadingHandler;
    }

    @PostConstruct
    public void warmup() {
        simulateReadings();
    }

    @Scheduled(fixedDelayString = "${simulation.sensors.delay-ms:15000}")
    public void simulateReadings() {
        LocalTime now = LocalTime.now();
        boolean isDay = now.getHour() >= 6 && now.getHour() < 18;

        List<com.ecotech.plantae.sensor.domain.entities.Sensor> sensors = sensorRepository.search(
                new SensorSearchCriteria(null, null, SensorStatus.ACTIVE, null, 0, 100)).content();

        sensors.forEach(sensor -> {
            List<SensorMetric> metrics = metricsFor(sensor.getType());
            metrics.forEach(metric -> ingestSensorReadingHandler.handle(new IngestSensorReadingCommand(
                    sensor.getId().value(),
                    Instant.now().toString(),
                    metric.apiName(),
                    generateRealisticValue(metric, isDay),
                    "AUTO",
                    null)));
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
                    SensorMetric.LIGHT);
        };
    }

    private double generateRealisticValue(SensorMetric metric, boolean isDay) {
        double variation = random.nextDouble(); // 0.0 a 1.0

        return switch (metric) {
            case SOILMOISTURE -> 50.0 + (variation * 10.0 - 5.0);

            case TEMPERATURE -> isDay
                    ? 22.0 + (variation * 6.0)
                    : 16.0 + (variation * 4.0);

            case HUMIDITY -> isDay
                    ? 40.0 + (variation * 15.0)
                    : 60.0 + (variation * 15.0);

            case LIGHT -> isDay
                    ? 400.0 + (variation * 500.0)
                    : variation * 20.0;
        };
    }
}
