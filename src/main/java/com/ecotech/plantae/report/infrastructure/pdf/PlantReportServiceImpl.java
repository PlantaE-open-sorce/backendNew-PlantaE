package com.ecotech.plantae.report.infrastructure.pdf;

import com.ecotech.plantae.alert.domain.repositories.AlertRepository;
import com.ecotech.plantae.alert.domain.entities.Alert;
import com.ecotech.plantae.alert.domain.repositories.AlertRepository;
import com.ecotech.plantae.plant.domain.entities.Plant;
import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;
import com.ecotech.plantae.report.domain.services.PlantReportService;
import com.ecotech.plantae.sensor.domain.entities.SensorReading;
import com.ecotech.plantae.sensor.domain.models.SensorReadingSearchCriteria;
import com.ecotech.plantae.sensor.domain.repositories.SensorReadingRepository;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorMetric;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlantReportServiceImpl implements PlantReportService {

    private final PlantRepository plantRepository;
    private final SensorReadingRepository sensorReadingRepository;
    private final AlertRepository alertRepository;

    public PlantReportServiceImpl(PlantRepository plantRepository,
                                  SensorReadingRepository sensorReadingRepository,
                                  AlertRepository alertRepository) {
        this.plantRepository = plantRepository;
        this.sensorReadingRepository = sensorReadingRepository;
        this.alertRepository = alertRepository;
    }

    @Override
    public ByteArrayOutputStream generatePlantPdf(String plantId, String from, String to, String ownerId, List<String> metricsCsv) {
        Plant plant = plantRepository.findByIdAndOwner(PlantId.of(plantId), ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Plant not found"));
        List<String> metrics = (metricsCsv == null || metricsCsv.isEmpty())
                ? List.of("soilMoisture", "temperature", "humidity", "light")
                : metricsCsv;
        Instant fromTs = parseInstant(from);
        Instant toTs = parseInstant(to);
        List<SensorMetric> sensorMetrics = metrics.stream()
                .map(value -> {
                    try {
                        return SensorMetric.from(value);
                    } catch (IllegalArgumentException ex) {
                        return null;
                    }
                })
                .filter(metric -> metric != null)
                .toList();
        List<SensorReading> readings = plant.getSensorId() == null ? List.of() :
                sensorReadingRepository.search(new SensorReadingSearchCriteria(
                        plant.getSensorId(),
                        fromTs,
                        toTs,
                        null,
                        0,
                        1000
                )).content();

        Map<SensorMetric, DoubleSummaryStats> stats = readings.stream()
                .collect(Collectors.groupingBy(SensorReading::getMetric,
                        Collectors.collectingAndThen(Collectors.summarizingDouble(SensorReading::getValue), DoubleSummaryStats::from)));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        writer.println("Plant Report");
        writer.printf("Plant: %s (%s)%n", plant.getName(), plant.getSpecies());
        writer.printf("Owner: %s%n", ownerId);
        writer.printf("Date range: %s - %s%n", from, to);
        writer.println();
        writer.println("Metric Summary");
        sensorMetrics.forEach(metric -> {
            DoubleSummaryStats summary = stats.get(metric);
            if (summary != null) {
                writer.printf("%s -> min: %.2f, avg: %.2f, max: %.2f, total readings: %d%n",
                        metric.apiName(), summary.min(), summary.avg(), summary.max(), summary.count());
            } else {
                writer.printf("%s -> no data%n", metric.name());
            }
        });
        writer.flush();
        return out;
    }

    @Override
    public ByteArrayOutputStream generatePlantCsv(String plantId, String from, String to, String ownerId) {
        Plant plant = plantRepository.findByIdAndOwner(PlantId.of(plantId), ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Plant not found"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        writer.println("timestamp,metric,value,quality");
        if (plant.getSensorId() != null) {
            sensorReadingRepository.search(new SensorReadingSearchCriteria(
                    plant.getSensorId(),
                    parseInstant(from),
                    parseInstant(to),
                    null,
                    0,
                    500
            )).content().forEach(reading -> writer.printf("%s,%s,%.2f,%s%n",
                    reading.getTimestamp(), reading.getMetric(), reading.getValue(), reading.getQuality()));
        }
        writer.flush();
        return out;
    }

    @Override
    public ByteArrayOutputStream generateSummaryPdf(String ownerId, String from, String to) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        writer.println("Summary Report");
        writer.printf("Owner: %s%n", ownerId);
        writer.printf("Date range: %s - %s%n", from, to);
        writer.flush();
        return out;
    }

    private Instant parseInstant(String value) {
        if (value == null || value.isBlank()) {
            return Instant.now();
        }
        try {
            return Instant.parse(value);
        } catch (Exception ignored) {
            try {
                return java.time.LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant();
            } catch (Exception inner) {
                throw new IllegalArgumentException("Invalid date format. Use ISO-8601 timestamps.", inner);
            }
        }
    }

    private record DoubleSummaryStats(double min, double max, double avg, long count) {
        static DoubleSummaryStats from(java.util.DoubleSummaryStatistics stats) {
            return new DoubleSummaryStats(stats.getMin(), stats.getMax(), stats.getAverage(), stats.getCount());
        }
    }
}
