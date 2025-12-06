package com.ecotech.plantae.report.application.internal.commands;

import java.util.List;

public record GeneratePlantPdfReportCommand(String plantId, String ownerId, String from, String to, List<String> metrics) {}
