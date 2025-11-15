package com.ecotech.plantae.iam.interfaces.rest;

import com.ecotech.plantae.iam.application.commands.ChangePasswordCommand;
import com.ecotech.plantae.iam.application.commands.DeleteAccountCommand;
import com.ecotech.plantae.iam.application.commands.ForgotPasswordCommand;
import com.ecotech.plantae.iam.application.commands.LoginUserCommand;
import com.ecotech.plantae.iam.application.commands.RegisterUserCommand;
import com.ecotech.plantae.iam.application.handlers.ChangePasswordHandler;
import com.ecotech.plantae.iam.application.handlers.DeleteAccountHandler;
import com.ecotech.plantae.iam.application.handlers.ForgotPasswordHandler;
import com.ecotech.plantae.iam.application.handlers.GetCurrentUserProfileHandler;
import com.ecotech.plantae.iam.application.handlers.LoginUserHandler;
import com.ecotech.plantae.iam.application.handlers.RegisterUserHandler;
import com.ecotech.plantae.iam.application.queries.GetCurrentUserProfileQuery;
import com.ecotech.plantae.iam.domain.dtos.AuthResponse;
import com.ecotech.plantae.iam.domain.dtos.ProfileResponse;
import com.ecotech.plantae.iam.domain.dtos.UserProfileDto;
import com.ecotech.plantae.iam.domain.entities.User;
import com.ecotech.plantae.iam.domain.repositories.UserRepository;
import com.ecotech.plantae.iam.domain.valueobjects.UserId;
import com.ecotech.plantae.shared.application.i18n.LanguageResolver;
import com.ecotech.plantae.shared.application.i18n.LocalizedMessageService;
import com.ecotech.plantae.shared.application.security.CurrentUserProvider;
import com.ecotech.plantae.shared.infrastructure.security.JwtService;
import com.ecotech.plantae.shared.interfaces.rest.MessageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/iam")
@Validated
@Tag(name = "BC: IAM")
public class IamController {

    private final RegisterUserHandler registerUserHandler;
    private final LoginUserHandler loginUserHandler;
    private final ForgotPasswordHandler forgotPasswordHandler;
    private final ChangePasswordHandler changePasswordHandler;
    private final DeleteAccountHandler deleteAccountHandler;
    private final GetCurrentUserProfileHandler getCurrentUserProfileHandler;
    private final JwtService jwtService;
    private final LanguageResolver languageResolver;
    private final LocalizedMessageService localizedMessageService;
    private final CurrentUserProvider currentUserProvider;
    private final UserRepository userRepository;

    public IamController(RegisterUserHandler registerUserHandler,
                         LoginUserHandler loginUserHandler,
                         ForgotPasswordHandler forgotPasswordHandler,
                         ChangePasswordHandler changePasswordHandler,
                         DeleteAccountHandler deleteAccountHandler,
                         GetCurrentUserProfileHandler getCurrentUserProfileHandler,
                         JwtService jwtService,
                         LanguageResolver languageResolver,
                         LocalizedMessageService localizedMessageService,
                         CurrentUserProvider currentUserProvider,
                         UserRepository userRepository) {
        this.registerUserHandler = registerUserHandler;
        this.loginUserHandler = loginUserHandler;
        this.forgotPasswordHandler = forgotPasswordHandler;
        this.changePasswordHandler = changePasswordHandler;
        this.deleteAccountHandler = deleteAccountHandler;
        this.getCurrentUserProfileHandler = getCurrentUserProfileHandler;
        this.jwtService = jwtService;
        this.languageResolver = languageResolver;
        this.localizedMessageService = localizedMessageService;
        this.currentUserProvider = currentUserProvider;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestHeader(value = "Accept-Language", required = false) String acceptLanguage,
                                                 @Valid @RequestBody RegisterUserRequest request) {
        if (!request.password().equals(request.confirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords do not match");
        }
        User user = registerUserHandler.handle(new RegisterUserCommand(
                request.email(),
                request.password(),
                request.displayName(),
                request.language(),
                request.accountType()
        ));
        Locale locale = languageResolver.resolve(acceptLanguage, user.getLanguage().name().toLowerCase());
        String token = generateToken(user);
        String message = localizedMessageService.getMessage("iam.register.success", locale);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token, message, user.getAccountType().name()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestHeader(value = "Accept-Language", required = false) String acceptLanguage,
                                              @Valid @RequestBody LoginRequest request) {
        try {
            User user = loginUserHandler.handle(new LoginUserCommand(request.email(), request.password()));
            Locale locale = languageResolver.resolve(acceptLanguage, user.getLanguage().name().toLowerCase());
            String token = generateToken(user);
            String message = localizedMessageService.getMessage("iam.login.success", locale);
            return ResponseEntity.ok(new AuthResponse(token, message, user.getAccountType().name()));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@RequestHeader(value = "Accept-Language", required = false) String acceptLanguage,
                                                          @Valid @RequestBody ForgotPasswordRequest request) {
        forgotPasswordHandler.handle(new ForgotPasswordCommand(request.email()));
        Locale locale = languageResolver.resolve(acceptLanguage, "en");
        String message = localizedMessageService.getMessage("iam.forgot.success", locale);
        return ResponseEntity.ok(new MessageResponse(message));
    }

    @PutMapping("/change-password")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<MessageResponse> changePassword(@RequestHeader(value = "Accept-Language", required = false) String acceptLanguage,
                                                          @Valid @RequestBody ChangePasswordRequest request) {
        String userId = currentUserProvider.getCurrentUserId()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
        String language = userRepository.findById(UserId.of(userId))
                .map(user -> user.getLanguage().name().toLowerCase())
                .orElse("en");
        changePasswordHandler.handle(new ChangePasswordCommand(userId, request.currentPassword(), request.newPassword()));
        Locale locale = languageResolver.resolve(acceptLanguage, language);
        String message = localizedMessageService.getMessage("iam.change.success", locale);
        return ResponseEntity.ok(new MessageResponse(message));
    }

    @DeleteMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<MessageResponse> deleteAccount(@RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        String userId = currentUserProvider.getCurrentUserId()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
        String language = userRepository.findById(UserId.of(userId))
                .map(user -> user.getLanguage().name().toLowerCase())
                .orElse("en");
        deleteAccountHandler.handle(new DeleteAccountCommand(userId));
        Locale locale = languageResolver.resolve(acceptLanguage, language);
        String message = localizedMessageService.getMessage("iam.delete.success", locale);
        return ResponseEntity.ok(new MessageResponse(message));
    }

    @GetMapping("/profile")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ProfileResponse> profile(@RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        String userId = currentUserProvider.getCurrentUserId()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
        UserProfileDto dto = getCurrentUserProfileHandler.handle(new GetCurrentUserProfileQuery(userId));
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        String language = userRepository.findById(UserId.of(userId))
                .map(user -> user.getLanguage().name().toLowerCase())
                .orElse("en");
        Locale locale = languageResolver.resolve(acceptLanguage, language);
        String message = localizedMessageService.getMessage("iam.profile.success", locale);
        return ResponseEntity.ok(new ProfileResponse(message, dto));
    }

    private String generateToken(User user) {
        return jwtService.generateToken(
                user.getEmail(),
                Map.of(
                        "userId", user.getId().value(),
                        "role", user.getRole().name(),
                        "accountType", user.getAccountType().name(),
                        "language", user.getLanguage().name().toLowerCase()
                )
        );
    }

    public record RegisterUserRequest(
            @NotBlank @Email String email,
            @NotBlank @Size(min = 8, max = 100) String password,
            @NotBlank @Size(min = 8, max = 100) String confirmPassword,
            @NotBlank @Size(max = 100) String displayName,
            @NotBlank String language,
            @NotBlank @Pattern(regexp = "HOME|VIVERO_FORESTAL") String accountType
    ) {
    }

    public record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank @Size(min = 8, max = 100) String password
    ) {
    }

    public record ForgotPasswordRequest(
            @NotBlank @Email String email
    ) {
    }

    public record ChangePasswordRequest(
            @NotBlank @Size(min = 8, max = 100) String currentPassword,
            @NotBlank @Size(min = 8, max = 100) String newPassword
    ) {
    }
}
