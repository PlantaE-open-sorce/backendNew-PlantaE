package com.ecotech.plantae.plant.infrastructure.persistence.repository;

import com.ecotech.plantae.plant.domain.entities.Plant;
import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import com.ecotech.plantae.plant.domain.repositories.models.PagedResult;
import com.ecotech.plantae.plant.domain.repositories.models.PlantSearchCriteria;
import com.ecotech.plantae.plant.domain.valueobjects.PlantId;
import com.ecotech.plantae.plant.infrastructure.persistence.PlantJpaEntity;
import com.ecotech.plantae.plant.infrastructure.persistence.mappers.PlantJpaMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.ecotech.plantae.plant.infrastructure.persistence.repository.PlantSpecifications.fromCriteria;

@Repository
public class PlantRepositoryJpa implements PlantRepository {

    private final SpringDataPlantRepository springDataRepository;
    private final PlantJpaMapper mapper;

    public PlantRepositoryJpa(SpringDataPlantRepository springDataRepository, PlantJpaMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public Plant save(Plant plant) {
        return mapper.toDomain(springDataRepository.save(mapper.toJpa(plant)));
    }

    @Override
    public Optional<Plant> findById(PlantId id) {
        return springDataRepository.findById(id.value())
                .filter(entity -> entity.getDeletedAt() == null)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Plant> findByIdAndOwner(PlantId id, String ownerId) {
        return springDataRepository.findByIdAndOwnerId(id.value(), ownerId)
                .filter(entity -> entity.getDeletedAt() == null)
                .map(mapper::toDomain);
    }

    @Override
    public PagedResult<Plant> search(PlantSearchCriteria criteria) {
        Pageable pageable = buildPageable(criteria);
        Specification<PlantJpaEntity> specification = fromCriteria(criteria);
        Page<PlantJpaEntity> page = springDataRepository.findAll(specification, pageable);
        return new PagedResult<>(
                page.map(mapper::toDomain).toList(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages()
        );
    }

    private Pageable buildPageable(PlantSearchCriteria criteria) {
        String sortValue = criteria.sort();
        Sort sort = Sort.by("createdAt").descending();
        if (sortValue != null && !sortValue.isBlank()) {
            String[] parts = sortValue.split(",");
            Sort.Direction direction = parts.length > 1 && parts[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            sort = Sort.by(direction, parts[0]);
        }
        return PageRequest.of(Math.max(criteria.page(), 0), Math.max(criteria.size(), 1), sort);
    }
}
