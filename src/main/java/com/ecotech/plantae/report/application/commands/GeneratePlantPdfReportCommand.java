package com.ecotech.plantae.report.application.commands;

import java.util.List;

public record GeneratePlantPdfReportCommand(String plantId, String ownerId, String from, String to, List<String> metrics) {}
