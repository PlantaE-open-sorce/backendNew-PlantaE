package com.ecotech.plantae.report.application.handlers;

import com.ecotech.plantae.report.application.commands.GenerateSummaryPdfReportCommand;
import com.ecotech.plantae.report.domain.services.PlantReportService;

import java.io.ByteArrayOutputStream;

public class GenerateSummaryPdfReportHandler {

    private final PlantReportService plantReportService;

    public GenerateSummaryPdfReportHandler(PlantReportService plantReportService) {
        this.plantReportService = plantReportService;
    }

    public ByteArrayOutputStream handle(GenerateSummaryPdfReportCommand command) {
        return plantReportService.generateSummaryPdf(command.ownerId(), command.from(), command.to());
    }
}
