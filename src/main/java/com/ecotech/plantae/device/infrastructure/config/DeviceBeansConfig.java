package com.ecotech.plantae.device.infrastructure.config;

import com.ecotech.plantae.device.application.handlers.DeactivateDeviceHandler;
import com.ecotech.plantae.device.application.handlers.GetDeviceByIdHandler;
import com.ecotech.plantae.device.application.handlers.LinkDeviceToPlantHandler;
import com.ecotech.plantae.device.application.handlers.ListDevicesHandler;
import com.ecotech.plantae.device.application.handlers.RegisterDeviceHandler;
import com.ecotech.plantae.device.application.handlers.UpdateDeviceNoteHandler;
import com.ecotech.plantae.device.application.ports.PlantLinker;
import com.ecotech.plantae.device.domain.repositories.DeviceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeviceBeansConfig {

    @Bean
    public RegisterDeviceHandler registerDeviceHandler(DeviceRepository deviceRepository) {
        return new RegisterDeviceHandler(deviceRepository);
    }

    @Bean
    public DeactivateDeviceHandler deactivateDeviceHandler(DeviceRepository deviceRepository) {
        return new DeactivateDeviceHandler(deviceRepository);
    }

    @Bean
    public UpdateDeviceNoteHandler updateDeviceNoteHandler(DeviceRepository deviceRepository) {
        return new UpdateDeviceNoteHandler(deviceRepository);
    }

    @Bean
    public GetDeviceByIdHandler getDeviceByIdHandler(DeviceRepository deviceRepository) {
        return new GetDeviceByIdHandler(deviceRepository);
    }

    @Bean
    public LinkDeviceToPlantHandler linkDeviceToPlantHandler(DeviceRepository deviceRepository, PlantLinker plantLinker) {
        return new LinkDeviceToPlantHandler(deviceRepository, plantLinker);
    }

    @Bean
    public ListDevicesHandler listDevicesHandler(DeviceRepository deviceRepository) {
        return new ListDevicesHandler(deviceRepository);
    }
}
