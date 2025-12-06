package com.ecotech.plantae.shared.application.internal.simulation;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class HardwareProfileGenerator {

    private static final List<String> MODELS = List.of("HX-200", "HX-300", "SoilSense Mini", "LeafGuard 2.0");
    private static final List<String> STATUSES = List.of("OK", "MAINTENANCE", "WARN");
    private final SecureRandom secureRandom = new SecureRandom();

    public HardwareSnapshot generate(String seed) {
        return new HardwareSnapshot(
                seed != null && !seed.isBlank() ? seed : randomId(),
                randomFirmware(),
                randomPercentage(65, 100),
                randomPercentage(50, 100),
                STATUSES.get(secureRandom.nextInt(STATUSES.size()))
        );
    }

    public List<HardwareSnapshot> generateMany(int count) {
        List<HardwareSnapshot> snapshots = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            snapshots.add(generate(null));
        }
        return snapshots;
    }

    private String randomId() {
        return "HW-" + Integer.toHexString(secureRandom.nextInt(0xFFFF)).toUpperCase(Locale.ROOT);
    }

    private String randomFirmware() {
        int major = ThreadLocalRandom.current().nextInt(1, 4);
        int minor = ThreadLocalRandom.current().nextInt(0, 10);
        return MODELS.get(secureRandom.nextInt(MODELS.size())) + " v" + major + "." + minor;
    }

    private int randomPercentage(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public record HardwareSnapshot(String deviceId, String firmware, int batteryLevel, int signalQuality, String status) {
    }
}
