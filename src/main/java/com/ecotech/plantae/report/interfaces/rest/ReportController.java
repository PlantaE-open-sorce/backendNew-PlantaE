package com.ecotech.plantae.report.interfaces.rest;

import com.ecotech.plantae.report.application.commands.GeneratePlantCsvReportCommand;
import com.ecotech.plantae.report.application.commands.GeneratePlantPdfReportCommand;
import com.ecotech.plantae.report.application.commands.GenerateSummaryPdfReportCommand;
import com.ecotech.plantae.report.application.handlers.GeneratePlantCsvReportHandler;
import com.ecotech.plantae.report.application.handlers.GeneratePlantPdfReportHandler;
import com.ecotech.plantae.report.application.handlers.GenerateSummaryPdfReportHandler;
import com.ecotech.plantae.shared.application.security.CurrentUserProvider;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@Validated
@Tag(name = "BC: Report")
@SecurityRequirement(name = "bearerAuth")
public class ReportController {

    private final GeneratePlantPdfReportHandler plantPdfHandler;
    private final GeneratePlantCsvReportHandler plantCsvHandler;
    private final GenerateSummaryPdfReportHandler summaryPdfHandler;
    private final CurrentUserProvider currentUserProvider;

    public ReportController(GeneratePlantPdfReportHandler plantPdfHandler,
                            GeneratePlantCsvReportHandler plantCsvHandler,
                            GenerateSummaryPdfReportHandler summaryPdfHandler,
                            CurrentUserProvider currentUserProvider) {
        this.plantPdfHandler = plantPdfHandler;
        this.plantCsvHandler = plantCsvHandler;
        this.summaryPdfHandler = summaryPdfHandler;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping(value = "/plants/{plantId}.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> plantPdf(@PathVariable String plantId,
                                           @RequestParam @NotBlank String from,
                                           @RequestParam @NotBlank String to,
                                           @RequestParam(required = false) List<String> metrics,
                                           @RequestParam(required = false) String ownerId) {
        String requester = resolveOwner(ownerId);
        var out = plantPdfHandler.handle(new GeneratePlantPdfReportCommand(plantId, requester, from, to, metrics));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=plant-" + plantId + ".pdf")
                .body(out.toByteArray());
    }

    @GetMapping(value = "/plants/{plantId}.csv", produces = "text/csv")
    public ResponseEntity<byte[]> plantCsv(@PathVariable String plantId,
                                           @RequestParam @NotBlank String from,
                                           @RequestParam @NotBlank String to,
                                           @RequestParam(required = false) String ownerId) {
        String requester = resolveOwner(ownerId);
        var out = plantCsvHandler.handle(new GeneratePlantCsvReportCommand(plantId, requester, from, to));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=plant-" + plantId + ".csv")
                .body(out.toByteArray());
    }

    @GetMapping(value = "/summary.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> summary(@RequestParam @NotBlank String from,
                                          @RequestParam @NotBlank String to,
                                          @RequestParam(required = false) String ownerId) {
        String requester = resolveOwner(ownerId);
        var out = summaryPdfHandler.handle(new GenerateSummaryPdfReportCommand(requester, from, to));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=summary.pdf")
                .body(out.toByteArray());
    }

    private String resolveOwner(String requestedOwner) {
        String currentUser = currentUserProvider.getCurrentUserId()
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Unauthorized"));
        if (isAdmin() && requestedOwner != null && !requestedOwner.isBlank()) {
            return requestedOwner;
        }
        return currentUser;
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> "ROLE_ADMIN".equals(grantedAuthority.getAuthority()));
    }
}
