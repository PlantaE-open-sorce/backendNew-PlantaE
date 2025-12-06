package com.ecotech.plantae.home.interfaces.rest;

import com.ecotech.plantae.home.application.HomeDashboardService;
import com.ecotech.plantae.home.application.HomeManualActionService;
import com.ecotech.plantae.home.application.HomeManualActionService.RegisterManualActionCommand;
import com.ecotech.plantae.home.application.HomeDashboardService.HomeDashboardResponse;
import com.ecotech.plantae.home.application.HomeManualActionService.HomeManualActionDto;
import com.ecotech.plantae.iam.domain.valueobjects.UserAccountType;
import com.ecotech.plantae.shared.application.internal.i18n.LanguageResolver;
import com.ecotech.plantae.shared.application.internal.i18n.LocalizedMessageService;
import com.ecotech.plantae.shared.application.internal.security.AccountTypeGuard;
import com.ecotech.plantae.shared.application.internal.security.CurrentUserProvider;
import com.ecotech.plantae.shared.interfaces.rest.MessageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/home")
@SecurityRequirement(name = "bearerAuth")
@Validated
@Tag(name = "BC: Home")
public class HomeDashboardController {

    private final AccountTypeGuard accountTypeGuard;
    private final CurrentUserProvider currentUserProvider;
    private final HomeDashboardService homeDashboardService;
    private final HomeManualActionService homeManualActionService;
    private final LanguageResolver languageResolver;
    private final LocalizedMessageService localizedMessageService;

    public HomeDashboardController(AccountTypeGuard accountTypeGuard,
            CurrentUserProvider currentUserProvider,
            HomeDashboardService homeDashboardService,
            HomeManualActionService homeManualActionService,
            LanguageResolver languageResolver,
            LocalizedMessageService localizedMessageService) {
        this.accountTypeGuard = accountTypeGuard;
        this.currentUserProvider = currentUserProvider;
        this.homeDashboardService = homeDashboardService;
        this.homeManualActionService = homeManualActionService;
        this.languageResolver = languageResolver;
        this.localizedMessageService = localizedMessageService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<HomeDashboardResponse> dashboard() {
        accountTypeGuard.require(UserAccountType.HOME);
        String userId = requireUser();
        return ResponseEntity.ok(homeDashboardService.build(userId));
    }

    @GetMapping("/manual-actions")
    public ResponseEntity<List<HomeManualActionDto>> manualActions() {
        accountTypeGuard.require(UserAccountType.HOME);
        String userId = requireUser();
        return ResponseEntity.ok(homeManualActionService.list(userId));
    }

    @PostMapping("/manual-actions")
    public ResponseEntity<MessageResponse> registerManualAction(
            @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage,
            @Valid @RequestBody RegisterManualActionRequest request) {
        accountTypeGuard.require(UserAccountType.HOME);
        String userId = requireUser();
        homeManualActionService.register(userId, new RegisterManualActionCommand(
                request.actionType(),
                request.notes(),
                parseInstant(request.performedAt()),
                request.durationMinutes()));
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse(
                localizedMessageService.getMessage(
                        "home.manual-action.saved",
                        languageResolver.resolve(acceptLanguage, "es"))));
    }

    private String requireUser() {
        return currentUserProvider.getCurrentUserId()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
    }

    private Instant parseInstant(String iso) {
        if (iso == null || iso.isBlank()) {
            return null;
        }
        try {
            return Instant.parse(iso);
        } catch (DateTimeParseException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid timestamp format. Use ISO-8601.");
        }
    }

    public record RegisterManualActionRequest(
            @NotBlank @Size(max = 60) String actionType,
            @Size(max = 250) String notes,
            String performedAt,
            @Min(0) int durationMinutes) {
    }
}
