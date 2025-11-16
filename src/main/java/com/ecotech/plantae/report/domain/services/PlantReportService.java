package com.ecotech.plantae.report.domain.services;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface PlantReportService {
    ByteArrayOutputStream generatePlantPdf(String plantId, String from, String to, String ownerId, List<String> metrics);
    ByteArrayOutputStream generatePlantCsv(String plantId, String from, String to, String ownerId);
    ByteArrayOutputStream generateSummaryPdf(String ownerId, String from, String to);
}
