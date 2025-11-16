package com.ecotech.plantae.device.infrastructure.persistence.repository;

import com.ecotech.plantae.device.domain.entities.Device;
import com.ecotech.plantae.device.domain.repositories.DeviceRepository;
import com.ecotech.plantae.device.domain.valueobjects.DeviceId;
import com.ecotech.plantae.device.infrastructure.persistence.mappers.DeviceJpaMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DeviceRepositoryJpa implements DeviceRepository {

    private final SpringDataDeviceRepository springDataRepository;
    private final DeviceJpaMapper mapper;

    public DeviceRepositoryJpa(SpringDataDeviceRepository springDataRepository, DeviceJpaMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public Device save(Device device) {
        return mapper.toDomain(springDataRepository.save(mapper.toJpa(device)));
    }

    @Override
    public Optional<Device> findById(DeviceId id) {
        return springDataRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public boolean existsById(DeviceId id) {
        return springDataRepository.existsById(id.value());
    }

    @Override
    public List<Device> findByOwnerId(String ownerId) {
        return springDataRepository.findByOwnerId(ownerId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
