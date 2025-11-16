package com.ecotech.plantae.device.infrastructure.persistence.mappers;

import com.ecotech.plantae.device.domain.entities.Device;
import com.ecotech.plantae.device.domain.valueobjects.DeviceId;
import com.ecotech.plantae.device.domain.valueobjects.OwnerId;
import com.ecotech.plantae.device.infrastructure.persistence.DeviceJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class DeviceJpaMapper {

    public DeviceJpaEntity toJpa(Device device) {
        return new DeviceJpaEntity(
                device.getId().value(),
                device.getOwnerId().value(),
                device.getHwModel(),
                device.getSecret(),
                device.isActive(),
                device.getNote()
        );
    }

    public Device toDomain(DeviceJpaEntity entity) {
        return Device.restore(
                DeviceId.of(entity.getId()),
                OwnerId.of(entity.getOwnerId()),
                entity.getHwModel(),
                entity.getSecret(),
                entity.isActive(),
                entity.getNote()
        );
    }
}
