package com.ecotech.plantae.report.application.internal.handlers;

import com.ecotech.plantae.report.application.internal.commands.GeneratePlantPdfReportCommand;
import com.ecotech.plantae.report.domain.services.PlantReportService;

import java.io.ByteArrayOutputStream;

public class GeneratePlantPdfReportHandler {

    private final PlantReportService plantReportService;

    public GeneratePlantPdfReportHandler(PlantReportService plantReportService) {
        this.plantReportService = plantReportService;
    }

    public ByteArrayOutputStream handle(GeneratePlantPdfReportCommand command) {
        return plantReportService.generatePlantPdf(command.plantId(), command.from(), command.to(), command.ownerId(), command.metrics());
    }
}
