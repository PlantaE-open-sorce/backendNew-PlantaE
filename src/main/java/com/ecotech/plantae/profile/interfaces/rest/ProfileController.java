package com.ecotech.plantae.profile.interfaces.rest;

import com.ecotech.plantae.profile.application.commands.CreateProfileCommand;
import com.ecotech.plantae.profile.application.commands.UpdateProfileCommand;
import com.ecotech.plantae.profile.application.handlers.CreateProfileHandler;
import com.ecotech.plantae.profile.application.handlers.GetProfileBySlugHandler;
import com.ecotech.plantae.profile.application.handlers.UpdateProfileHandler;
import com.ecotech.plantae.profile.application.queries.GetProfileBySlugQuery;
import com.ecotech.plantae.profile.domain.dtos.ProfileDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/profiles")
@Validated
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "BC: Profile")
public class ProfileController {

    private final CreateProfileHandler createProfileHandler;
    private final UpdateProfileHandler updateProfileHandler;
    private final GetProfileBySlugHandler getProfileBySlugHandler;

    public ProfileController(CreateProfileHandler createProfileHandler,
                             UpdateProfileHandler updateProfileHandler,
                             GetProfileBySlugHandler getProfileBySlugHandler) {
        this.createProfileHandler = createProfileHandler;
        this.updateProfileHandler = updateProfileHandler;
        this.getProfileBySlugHandler = getProfileBySlugHandler;
    }

    @PostMapping
    public ResponseEntity<ProfileDto> create(@Valid @RequestBody CreateProfileRequest request) {
        try {
            ProfileDto dto = createProfileHandler.handle(new CreateProfileCommand(request.ownerId(), request.displayName()));
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        }
    }

    @PutMapping("/{ownerId}")
    public ResponseEntity<ProfileDto> update(@PathVariable String ownerId,
                                             @Valid @RequestBody UpdateProfileRequest request) {
        try {
            ProfileDto dto = updateProfileHandler.handle(new UpdateProfileCommand(
                    ownerId,
                    request.displayName(),
                    request.bio(),
                    request.avatarUrl(),
                    request.location(),
                    request.timezone(),
                    request.slug()
            ));
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ProfileDto> getBySlug(@PathVariable String slug) {
        ProfileDto dto = getProfileBySlugHandler.handle(new GetProfileBySlugQuery(slug));
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found");
        }
        return ResponseEntity.ok(dto);
    }

    public record CreateProfileRequest(
            @NotBlank String ownerId,
            @NotBlank @Size(max = 100) String displayName
    ) {
    }

    public record UpdateProfileRequest(
            @Size(max = 100) String displayName,
            @Size(max = 500) String bio,
            @Size(max = 255) String avatarUrl,
            @Size(max = 255) String location,
            @Size(max = 100) String timezone,
            @Size(min = 3, max = 50) String slug
    ) {
    }
}
