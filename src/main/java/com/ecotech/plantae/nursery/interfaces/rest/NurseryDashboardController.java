package com.ecotech.plantae.nursery.interfaces.rest;

import com.ecotech.plantae.iam.domain.valueobjects.UserAccountType;
import com.ecotech.plantae.nursery.application.NurseryDashboardService;
import com.ecotech.plantae.nursery.application.NurseryInputService;
import com.ecotech.plantae.nursery.application.NurseryInputService.RegisterInputCommand;
import com.ecotech.plantae.nursery.application.NurseryTaskService;
import com.ecotech.plantae.nursery.application.NurseryTaskService.ScheduleSpecificTaskCommand;
import com.ecotech.plantae.nursery.application.NurseryDashboardService.NurseryBatchCard;
import com.ecotech.plantae.nursery.application.NurseryDashboardService.NurseryDashboardResponse;
import com.ecotech.plantae.nursery.application.NurseryInputService.NurseryInputDto;
import com.ecotech.plantae.nursery.application.NurseryTaskService.NurseryTaskDto;
import com.ecotech.plantae.shared.application.internal.i18n.LanguageResolver;
import com.ecotech.plantae.shared.application.internal.i18n.LocalizedMessageService;
import com.ecotech.plantae.shared.application.internal.security.AccountTypeGuard;
import com.ecotech.plantae.shared.application.internal.security.CurrentUserProvider;
import com.ecotech.plantae.shared.interfaces.rest.MessageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/nursery")
@SecurityRequirement(name = "bearerAuth")
@Validated
@Tag(name = "BC: Nursery")
public class NurseryDashboardController {

    private final AccountTypeGuard accountTypeGuard;
    private final CurrentUserProvider currentUserProvider;
    private final NurseryDashboardService nurseryDashboardService;
    private final NurseryTaskService nurseryTaskService;
    private final NurseryInputService nurseryInputService;
    private final LanguageResolver languageResolver;
    private final LocalizedMessageService localizedMessageService;

    public NurseryDashboardController(AccountTypeGuard accountTypeGuard,
            CurrentUserProvider currentUserProvider,
            NurseryDashboardService nurseryDashboardService,
            NurseryTaskService nurseryTaskService,
            NurseryInputService nurseryInputService,
            LanguageResolver languageResolver,
            LocalizedMessageService localizedMessageService) {
        this.accountTypeGuard = accountTypeGuard;
        this.currentUserProvider = currentUserProvider;
        this.nurseryDashboardService = nurseryDashboardService;
        this.nurseryTaskService = nurseryTaskService;
        this.nurseryInputService = nurseryInputService;
        this.languageResolver = languageResolver;
        this.localizedMessageService = localizedMessageService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<NurseryDashboardResponse> dashboard() {
        accountTypeGuard.require(UserAccountType.VIVERO_FORESTAL);
        String userId = requireUser();
        return ResponseEntity.ok(nurseryDashboardService.build(userId));
    }

    @GetMapping("/batches")
    public ResponseEntity<List<NurseryBatchCard>> batches() {
        accountTypeGuard.require(UserAccountType.VIVERO_FORESTAL);
        String userId = requireUser();
        return ResponseEntity.ok(nurseryDashboardService.batches(userId));
    }

    @GetMapping("/tasks/todo")
    public ResponseEntity<List<NurseryTaskDto>> tasks() {
        accountTypeGuard.require(UserAccountType.VIVERO_FORESTAL);
        String userId = requireUser();
        return ResponseEntity.ok(nurseryTaskService.listTodos(userId));
    }

    @PostMapping("/tasks/specific")
    public ResponseEntity<NurseryTaskDto> createSpecificTask(@Valid @RequestBody ScheduleSpecificTaskRequest request) {
        accountTypeGuard.require(UserAccountType.VIVERO_FORESTAL);
        String userId = requireUser();
        NurseryTaskDto dto = nurseryTaskService.schedule(userId, new ScheduleSpecificTaskCommand(
                request.title(),
                request.assetId(),
                request.assetType(),
                request.dueDate(),
                request.priority(),
                request.notes()));
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/inputs")
    public ResponseEntity<List<NurseryInputDto>> inputs() {
        accountTypeGuard.require(UserAccountType.VIVERO_FORESTAL);
        String userId = requireUser();
        return ResponseEntity.ok(nurseryInputService.listRecent(userId));
    }

    @PostMapping("/inputs")
    public ResponseEntity<MessageResponse> registerInput(
            @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage,
            @Valid @RequestBody RegisterInputRequest request) {
        accountTypeGuard.require(UserAccountType.VIVERO_FORESTAL);
        String userId = requireUser();
        nurseryInputService.register(userId, new RegisterInputCommand(
                request.assetId(),
                request.assetType(),
                request.inputType(),
                request.quantity(),
                request.unit(),
                request.cost(),
                request.appliedAt(),
                request.appliedBy()));
        String message = localizedMessageService.getMessage(
                "nursery.input.saved",
                languageResolver.resolve(acceptLanguage, "es"));
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse(message));
    }

    private String requireUser() {
        return currentUserProvider.getCurrentUserId()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
    }

    public record ScheduleSpecificTaskRequest(
            @NotBlank @Size(max = 120) String title,
            @NotBlank String assetId,
            @NotBlank @Pattern(regexp = "PLANT|BATCH") String assetType,
            String dueDate,
            @NotBlank @Pattern(regexp = "LOW|MEDIUM|HIGH") String priority,
            @Size(max = 500) String notes) {
    }

    public record RegisterInputRequest(
            @NotBlank String assetId,
            @NotBlank @Pattern(regexp = "PLANT|BATCH") String assetType,
            @NotBlank String inputType,
            @DecimalMin(value = "0.0", inclusive = false) double quantity,
            @NotBlank String unit,
            @DecimalMin(value = "0.0", inclusive = false) double cost,
            String appliedAt,
            @NotBlank String appliedBy) {
    }
}
