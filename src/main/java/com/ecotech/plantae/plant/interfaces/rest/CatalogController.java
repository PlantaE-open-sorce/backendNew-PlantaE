package com.ecotech.plantae.plant.interfaces.rest;

import com.ecotech.plantae.plant.application.internal.services.SpeciesCatalogService;
import com.ecotech.plantae.plant.application.internal.services.SpeciesCatalogService.Category;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalogs")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "BC: Catalogs")
public class CatalogController {

    private final SpeciesCatalogService speciesCatalogService;

    public CatalogController(SpeciesCatalogService speciesCatalogService) {
        this.speciesCatalogService = speciesCatalogService;
    }

    @GetMapping("/species")
    public List<Category> species() {
        return speciesCatalogService.getCatalog();
    }
}
