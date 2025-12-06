package com.ecotech.plantae.sensor.application.internal.handlers;

import com.ecotech.plantae.sensor.application.internal.commands.IngestSensorReadingCommand;
import com.ecotech.plantae.sensor.domain.entities.SensorReading;
import com.ecotech.plantae.sensor.domain.repositories.SensorReadingRepository;
import com.ecotech.plantae.sensor.domain.repositories.SensorRepository;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorId;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorMetric;
import com.ecotech.plantae.shared.infrastructure.realtime.ReadingEventPayload;
import com.ecotech.plantae.shared.infrastructure.realtime.RealtimeEmitter;
import com.ecotech.plantae.alert.application.internal.commands.RaiseAlertCommand;
import com.ecotech.plantae.alert.application.internal.handlers.RaiseAlertHandler;
import com.ecotech.plantae.alert.domain.valueobjects.AlertType;

import java.time.Instant;
import java.util.Map;

public class IngestSensorReadingHandler {

    private final SensorRepository sensorRepository;
    private final SensorReadingRepository sensorReadingRepository;
    private final RealtimeEmitter realtimeEmitter;
    private final RaiseAlertHandler raiseAlertHandler;
    private final Map<SensorMetric, Threshold> thresholds = Map.of(
            SensorMetric.SOILMOISTURE, new Threshold(20d, 80d),
            SensorMetric.TEMPERATURE, new Threshold(10d, 35d),
            SensorMetric.HUMIDITY, new Threshold(30d, 80d),
            SensorMetric.LIGHT, new Threshold(100d, 900d)
    );

    public IngestSensorReadingHandler(SensorRepository sensorRepository,
                                      SensorReadingRepository sensorReadingRepository,
                                      RealtimeEmitter realtimeEmitter,
                                      RaiseAlertHandler raiseAlertHandler) {
        this.sensorRepository = sensorRepository;
        this.sensorReadingRepository = sensorReadingRepository;
        this.realtimeEmitter = realtimeEmitter;
        this.raiseAlertHandler = raiseAlertHandler;
    }

    public void handle(IngestSensorReadingCommand command) {
        var sensor = sensorRepository.findById(SensorId.of(command.sensorId()))
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found"));
        Instant timestamp = command.timestamp() != null && !command.timestamp().isBlank()
                ? Instant.parse(command.timestamp())
                : Instant.now();
        SensorReading reading = new SensorReading(sensor.getId(), timestamp,
                SensorMetric.from(command.metric()), command.value(), command.quality());
        sensorReadingRepository.save(reading);
        sensor.markReading(timestamp);
        sensorRepository.save(sensor);
        realtimeEmitter.publishReading(sensor.getOwnerId(), new ReadingEventPayload(
                sensor.getId().value(),
                sensor.getPlantId() != null ? sensor.getPlantId().value() : null,
                reading.getMetric().apiName(),
                reading.getValue(),
                reading.getTimestamp().toString()
        ));
        checkThreshold(sensor, reading, command.acceptLanguage());
    }

    private void checkThreshold(com.ecotech.plantae.sensor.domain.entities.Sensor sensor, SensorReading reading, String acceptLanguage) {
        Threshold threshold = thresholds.get(reading.getMetric());
        if (threshold == null || sensor.getPlantId() == null) return;
        double value = reading.getValue();
        if (value < threshold.min() || value > threshold.max()) {
            double breached = value < threshold.min() ? threshold.min() : threshold.max();
            raiseAlertHandler.handle(new RaiseAlertCommand(
                    sensor.getPlantId().value(),
                    sensor.getId().value(),
                    sensor.getOwnerId(),
                    AlertType.THRESHOLD_BREACH.name(),
                    reading.getMetric().apiName(),
                    value,
                    breached,
                    acceptLanguage
            ));
        }
    }

    private record Threshold(double min, double max) {}
}
