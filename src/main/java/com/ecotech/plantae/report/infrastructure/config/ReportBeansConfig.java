package com.ecotech.plantae.report.infrastructure.config;

import com.ecotech.plantae.report.application.internal.handlers.GeneratePlantCsvReportHandler;
import com.ecotech.plantae.report.application.internal.handlers.GeneratePlantPdfReportHandler;
import com.ecotech.plantae.report.application.internal.handlers.GenerateSummaryPdfReportHandler;
import com.ecotech.plantae.report.domain.services.PlantReportService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReportBeansConfig {

    @Bean
    public GeneratePlantPdfReportHandler generatePlantPdfReportHandler(PlantReportService plantReportService) {
        return new GeneratePlantPdfReportHandler(plantReportService);
    }

    @Bean
    public GeneratePlantCsvReportHandler generatePlantCsvReportHandler(PlantReportService plantReportService) {
        return new GeneratePlantCsvReportHandler(plantReportService);
    }

    @Bean
    public GenerateSummaryPdfReportHandler generateSummaryPdfReportHandler(PlantReportService plantReportService) {
        return new GenerateSummaryPdfReportHandler(plantReportService);
    }
}
