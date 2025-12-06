package com.ecotech.plantae.report.infrastructure.pdf;

import com.ecotech.plantae.plant.domain.entities.Plant;
import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;
import com.ecotech.plantae.report.domain.services.PlantReportService;
import com.ecotech.plantae.sensor.domain.entities.SensorReading;
import com.ecotech.plantae.sensor.domain.models.SensorReadingSearchCriteria;
import com.ecotech.plantae.sensor.domain.repositories.SensorReadingRepository;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorMetric;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlantReportServiceImpl implements PlantReportService {

    private final PlantRepository plantRepository;
    private final SensorReadingRepository sensorReadingRepository;

    // Colores del tema PlantaE
    private static final Color PRIMARY_GREEN = new Color(34, 139, 34);
    private static final Color DARK_TEXT = new Color(33, 33, 33);
    private static final Color GRAY_TEXT = new Color(100, 100, 100);
    private static final Color TABLE_HEADER_BG = new Color(76, 175, 80);
    private static final Color TABLE_ALT_ROW = new Color(245, 250, 245);

    // Fuentes
    private static final Font TITLE_FONT = new Font(Font.HELVETICA, 24, Font.BOLD, PRIMARY_GREEN);
    private static final Font SUBTITLE_FONT = new Font(Font.HELVETICA, 14, Font.NORMAL, GRAY_TEXT);
    private static final Font SECTION_FONT = new Font(Font.HELVETICA, 16, Font.BOLD, DARK_TEXT);
    private static final Font NORMAL_FONT = new Font(Font.HELVETICA, 11, Font.NORMAL, DARK_TEXT);
    private static final Font BOLD_FONT = new Font(Font.HELVETICA, 11, Font.BOLD, DARK_TEXT);
    private static final Font TABLE_HEADER_FONT = new Font(Font.HELVETICA, 11, Font.BOLD, Color.WHITE);
    private static final Font SMALL_FONT = new Font(Font.HELVETICA, 9, Font.NORMAL, GRAY_TEXT);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    public PlantReportServiceImpl(PlantRepository plantRepository,
            SensorReadingRepository sensorReadingRepository) {
        this.plantRepository = plantRepository;
        this.sensorReadingRepository = sensorReadingRepository;
    }

    @Override
    public ByteArrayOutputStream generatePlantPdf(String plantId, String from, String to, String ownerId,
            List<String> metricsCsv) {
        Plant plant = plantRepository.findByIdAndOwner(PlantId.of(plantId), ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Plant not found"));

        List<String> metrics = (metricsCsv == null || metricsCsv.isEmpty())
                ? List.of("soilMoisture", "temperature", "humidity", "light")
                : metricsCsv;

        Instant toTs = parseInstantOrDefault(to, Instant.now());
        Instant fromTs = parseInstantOrDefault(from, toTs.minusSeconds(72 * 3600));

        List<SensorMetric> sensorMetrics = metrics.stream()
                .map(value -> {
                    try {
                        return SensorMetric.from(value);
                    } catch (IllegalArgumentException ex) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        List<SensorReading> readings = plant.getSensorId() == null ? List.of()
                : sensorReadingRepository.search(new SensorReadingSearchCriteria(
                        plant.getSensorId(),
                        fromTs,
                        toTs,
                        null,
                        0,
                        1000)).content();

        Map<SensorMetric, DoubleSummaryStats> stats = readings.stream()
                .collect(Collectors.groupingBy(SensorReading::getMetric,
                        Collectors.collectingAndThen(Collectors.summarizingDouble(SensorReading::getValue),
                                DoubleSummaryStats::from)));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Header con logo/título
            addHeader(document, "Reporte de Planta");

            // Info de la planta
            addPlantInfo(document, plant, fromTs, toTs);

            // Tabla de resumen de métricas
            addMetricsSummaryTable(document, sensorMetrics, stats);

            // Gráfico de barras
            if (!stats.isEmpty()) {
                addBarChart(document, sensorMetrics, stats);
            }

            // Footer
            addFooter(document);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generating PDF", e);
        }

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
            Instant toTs = parseInstantOrDefault(to, Instant.now());
            Instant fromTs = parseInstantOrDefault(from, toTs.minusSeconds(72 * 3600));
            sensorReadingRepository.search(new SensorReadingSearchCriteria(
                    plant.getSensorId(),
                    fromTs,
                    toTs,
                    null,
                    0,
                    500)).content().forEach(reading -> writer.printf("%s,%s,%.2f,%s%n",
                            reading.getTimestamp(), reading.getMetric(), reading.getValue(), reading.getQuality()));
        }
        writer.flush();
        return out;
    }

    @Override
    public ByteArrayOutputStream generateSummaryPdf(String ownerId, String from, String to) {
        Instant toTs = parseInstantOrDefault(to, Instant.now());
        Instant fromTs = parseInstantOrDefault(from, toTs.minusSeconds(72 * 3600));

        List<Plant> plants = plantRepository.findByOwner(ownerId);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Header
            addHeader(document, "Resumen General");

            // Info general
            Paragraph info = new Paragraph();
            info.add(new Chunk("Período: ", BOLD_FONT));
            info.add(new Chunk(DATE_FORMATTER.format(fromTs) + " - " + DATE_FORMATTER.format(toTs), NORMAL_FONT));
            info.setSpacingAfter(10);
            document.add(info);

            Paragraph plantCount = new Paragraph();
            plantCount.add(new Chunk("Total de plantas: ", BOLD_FONT));
            plantCount.add(new Chunk(String.valueOf(plants.size()), NORMAL_FONT));
            plantCount.setSpacingAfter(20);
            document.add(plantCount);

            // Tabla de plantas
            if (!plants.isEmpty()) {
                addPlantsTable(document, plants, fromTs, toTs);
            } else {
                document.add(new Paragraph("No hay plantas registradas.", NORMAL_FONT));
            }

            // Footer
            addFooter(document);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generating PDF", e);
        }

        return out;
    }

    private void addHeader(Document document, String title) throws DocumentException {
        // Título principal
        Paragraph header = new Paragraph();
        header.add(new Chunk("🌱 PlantaE", TITLE_FONT));
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);

        // Subtítulo
        Paragraph subtitle = new Paragraph(title, SUBTITLE_FONT);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(20);
        document.add(subtitle);

        // Línea separadora
        PdfPTable line = new PdfPTable(1);
        line.setWidthPercentage(100);
        PdfPCell lineCell = new PdfPCell();
        lineCell.setBorderWidth(0);
        lineCell.setBorderWidthBottom(2);
        lineCell.setBorderColorBottom(PRIMARY_GREEN);
        lineCell.setFixedHeight(5);
        line.addCell(lineCell);
        document.add(line);
        document.add(Chunk.NEWLINE);
    }

    private void addPlantInfo(Document document, Plant plant, Instant from, Instant to) throws DocumentException {
        Paragraph section = new Paragraph("Información de la Planta", SECTION_FONT);
        section.setSpacingAfter(10);
        document.add(section);

        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setWidths(new float[] { 1, 2 });

        addInfoRow(infoTable, "Nombre:", plant.getName());
        addInfoRow(infoTable, "Especie:", plant.getSpecies());
        addInfoRow(infoTable, "Ubicación:", plant.getLocation() != null ? plant.getLocation() : "No especificada");
        addInfoRow(infoTable, "Período:", DATE_FORMATTER.format(from) + " - " + DATE_FORMATTER.format(to));

        document.add(infoTable);
        document.add(Chunk.NEWLINE);
    }

    private void addInfoRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, BOLD_FONT));
        labelCell.setBorder(0);
        labelCell.setPadding(5);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, NORMAL_FONT));
        valueCell.setBorder(0);
        valueCell.setPadding(5);
        table.addCell(valueCell);
    }

    private void addMetricsSummaryTable(Document document, List<SensorMetric> metrics,
            Map<SensorMetric, DoubleSummaryStats> stats) throws DocumentException {
        Paragraph section = new Paragraph("Resumen de Métricas", SECTION_FONT);
        section.setSpacingBefore(10);
        section.setSpacingAfter(10);
        document.add(section);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[] { 2, 1.5f, 1.5f, 1.5f, 1 });

        // Headers
        String[] headers = { "Métrica", "Mínimo", "Promedio", "Máximo", "Lecturas" };
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, TABLE_HEADER_FONT));
            cell.setBackgroundColor(TABLE_HEADER_BG);
            cell.setPadding(8);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        // Datos
        boolean alternate = false;
        for (SensorMetric metric : metrics) {
            DoubleSummaryStats summary = stats.get(metric);
            Color bgColor = alternate ? TABLE_ALT_ROW : Color.WHITE;

            addDataCell(table, getMetricDisplayName(metric), bgColor, Element.ALIGN_LEFT);

            if (summary != null) {
                addDataCell(table, String.format("%.2f %s", summary.min(), getMetricUnit(metric)), bgColor,
                        Element.ALIGN_CENTER);
                addDataCell(table, String.format("%.2f %s", summary.avg(), getMetricUnit(metric)), bgColor,
                        Element.ALIGN_CENTER);
                addDataCell(table, String.format("%.2f %s", summary.max(), getMetricUnit(metric)), bgColor,
                        Element.ALIGN_CENTER);
                addDataCell(table, String.valueOf(summary.count()), bgColor, Element.ALIGN_CENTER);
            } else {
                addDataCell(table, "-", bgColor, Element.ALIGN_CENTER);
                addDataCell(table, "-", bgColor, Element.ALIGN_CENTER);
                addDataCell(table, "-", bgColor, Element.ALIGN_CENTER);
                addDataCell(table, "0", bgColor, Element.ALIGN_CENTER);
            }
            alternate = !alternate;
        }

        document.add(table);
        document.add(Chunk.NEWLINE);
    }

    private void addDataCell(PdfPTable table, String text, Color bgColor, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, NORMAL_FONT));
        cell.setBackgroundColor(bgColor);
        cell.setPadding(6);
        cell.setHorizontalAlignment(alignment);
        table.addCell(cell);
    }

    private void addBarChart(Document document, List<SensorMetric> metrics, Map<SensorMetric, DoubleSummaryStats> stats)
            throws DocumentException {
        Paragraph section = new Paragraph("Gráfico de Promedios", SECTION_FONT);
        section.setSpacingBefore(15);
        section.setSpacingAfter(10);
        document.add(section);

        // Gráfico de barras simple usando tablas
        PdfPTable chartTable = new PdfPTable(2);
        chartTable.setWidthPercentage(80);
        chartTable.setWidths(new float[] { 1, 3 });

        // Encontrar el valor máximo para normalizar las barras
        double maxAvg = stats.values().stream()
                .mapToDouble(DoubleSummaryStats::avg)
                .max()
                .orElse(100);

        Color[] barColors = {
                new Color(76, 175, 80), // Verde
                new Color(255, 152, 0), // Naranja
                new Color(33, 150, 243), // Azul
                new Color(156, 39, 176) // Púrpura
        };

        int colorIndex = 0;
        for (SensorMetric metric : metrics) {
            DoubleSummaryStats summary = stats.get(metric);
            if (summary == null)
                continue;

            // Label
            PdfPCell labelCell = new PdfPCell(new Phrase(getMetricDisplayName(metric), NORMAL_FONT));
            labelCell.setBorder(0);
            labelCell.setPadding(5);
            labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            chartTable.addCell(labelCell);

            // Barra
            PdfPCell barCell = new PdfPCell();
            barCell.setBorder(0);
            barCell.setPadding(5);

            float barWidth = (float) (summary.avg() / maxAvg * 100);
            PdfPTable barTable = new PdfPTable(2);
            barTable.setWidthPercentage(100);
            barTable.setWidths(new float[] { barWidth, 100 - barWidth });

            PdfPCell filledBar = new PdfPCell(new Phrase(String.format("%.1f", summary.avg()),
                    new Font(Font.HELVETICA, 9, Font.BOLD, Color.WHITE)));
            filledBar.setBackgroundColor(barColors[colorIndex % barColors.length]);
            filledBar.setBorder(0);
            filledBar.setPadding(4);
            filledBar.setMinimumHeight(20);
            barTable.addCell(filledBar);

            PdfPCell emptyBar = new PdfPCell(new Phrase(""));
            emptyBar.setBackgroundColor(new Color(240, 240, 240));
            emptyBar.setBorder(0);
            emptyBar.setMinimumHeight(20);
            barTable.addCell(emptyBar);

            barCell.addElement(barTable);
            chartTable.addCell(barCell);

            colorIndex++;
        }

        document.add(chartTable);
    }

    private void addPlantsTable(Document document, List<Plant> plants, Instant from, Instant to)
            throws DocumentException {
        Paragraph section = new Paragraph("Lista de Plantas", SECTION_FONT);
        section.setSpacingBefore(10);
        section.setSpacingAfter(10);
        document.add(section);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[] { 2, 2, 2, 1 });

        // Headers
        String[] headers = { "Nombre", "Especie", "Ubicación", "Alertas" };
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, TABLE_HEADER_FONT));
            cell.setBackgroundColor(TABLE_HEADER_BG);
            cell.setPadding(8);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        boolean alternate = false;
        for (Plant plant : plants) {
            Color bgColor = alternate ? TABLE_ALT_ROW : Color.WHITE;

            addDataCell(table, plant.getName(), bgColor, Element.ALIGN_LEFT);
            addDataCell(table, plant.getSpecies(), bgColor, Element.ALIGN_LEFT);
            addDataCell(table, plant.getLocation() != null ? plant.getLocation() : "-", bgColor, Element.ALIGN_LEFT);
            addDataCell(table, plant.hasAlerts() ? "⚠️ Sí" : "✓ No", bgColor, Element.ALIGN_CENTER);

            alternate = !alternate;
        }

        document.add(table);
    }

    private void addFooter(Document document) throws DocumentException {
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        Paragraph footer = new Paragraph();
        footer.add(new Chunk("Generado por PlantaE • ", SMALL_FONT));
        footer.add(new Chunk(DATE_FORMATTER.format(Instant.now()), SMALL_FONT));
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }

    private String getMetricDisplayName(SensorMetric metric) {
        return switch (metric) {
            case TEMPERATURE -> "🌡️ Temperatura";
            case HUMIDITY -> "💧 Humedad Ambiental";
            case SOILMOISTURE -> "🌱 Humedad del Suelo";
            case LIGHT -> "☀️ Luminosidad";
        };
    }

    private String getMetricUnit(SensorMetric metric) {
        return switch (metric) {
            case TEMPERATURE -> "°C";
            case HUMIDITY, SOILMOISTURE -> "%";
            case LIGHT -> "lux";
        };
    }

    private Instant parseInstantOrDefault(String value, Instant defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
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
        static DoubleSummaryStats from(DoubleSummaryStatistics stats) {
            return new DoubleSummaryStats(stats.getMin(), stats.getMax(), stats.getAverage(), stats.getCount());
        }
    }
}
