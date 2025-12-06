package com.ecotech.plantae.report.application.internal.handlers;

import com.ecotech.plantae.report.application.internal.commands.GeneratePlantCsvReportCommand;
import com.ecotech.plantae.report.domain.services.PlantReportService;

import java.io.ByteArrayOutputStream;

public class GeneratePlantCsvReportHandler {

    private final PlantReportService plantReportService;

    public GeneratePlantCsvReportHandler(PlantReportService plantReportService) {
        this.plantReportService = plantReportService;
    }

    public ByteArrayOutputStream handle(GeneratePlantCsvReportCommand command) {
        return plantReportService.generatePlantCsv(command.plantId(), command.from(), command.to(), command.ownerId());
    }
}
