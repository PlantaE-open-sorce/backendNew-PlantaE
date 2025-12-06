package com.ecotech.plantae.report.application.internal.commands;

public record GeneratePlantCsvReportCommand(String plantId, String ownerId, String from, String to) {}
