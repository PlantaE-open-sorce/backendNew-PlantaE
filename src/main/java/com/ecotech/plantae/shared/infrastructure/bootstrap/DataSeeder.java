package com.ecotech.plantae.shared.infrastructure.bootstrap;

import com.ecotech.plantae.alert.domain.entities.Alert;
import com.ecotech.plantae.alert.domain.repositories.AlertRepository;
import com.ecotech.plantae.alert.domain.valueobjects.AlertType;
import com.ecotech.plantae.iam.application.internal.outboundservices.PasswordHasher;
import com.ecotech.plantae.iam.domain.entities.User;
import com.ecotech.plantae.iam.domain.repositories.UserRepository;
import com.ecotech.plantae.iam.domain.valueobjects.UserAccountType;
import com.ecotech.plantae.iam.domain.valueobjects.UserLanguage;
import com.ecotech.plantae.iam.domain.valueobjects.UserId;
import com.ecotech.plantae.plant.domain.entities.Plant;
import com.ecotech.plantae.plant.domain.repositories.PlantRepository;
import com.ecotech.plantae.profile.domain.entities.Profile;
import com.ecotech.plantae.profile.domain.repositories.ProfileRepository;
import com.ecotech.plantae.profile.domain.valueobjects.ProfileSlug;
import com.ecotech.plantae.sensor.domain.entities.Sensor;
import com.ecotech.plantae.sensor.domain.entities.SensorReading;
import com.ecotech.plantae.sensor.domain.repositories.SensorReadingRepository;
import com.ecotech.plantae.sensor.domain.repositories.SensorRepository;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorMetric;
import com.ecotech.plantae.sensor.domain.valueobjects.SensorType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Locale;
import java.text.Normalizer;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final PlantRepository plantRepository;
    private final SensorRepository sensorRepository;
    private final SensorReadingRepository sensorReadingRepository;
    private final AlertRepository alertRepository;
    private final ProfileRepository profileRepository;
    private final Random random = new Random(42); // Seed fijo para datos reproducibles

    public DataSeeder(UserRepository userRepository,
            PasswordHasher passwordHasher,
            PlantRepository plantRepository,
            SensorRepository sensorRepository,
            SensorReadingRepository sensorReadingRepository,
            AlertRepository alertRepository,
            ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.plantRepository = plantRepository;
        this.sensorRepository = sensorRepository;
        this.sensorReadingRepository = sensorReadingRepository;
        this.alertRepository = alertRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public void run(String... args) {
        // Usuario 1: Hogar con plantas tropicales
        seedUserWithPlants(
                "camila.rojas@plantae.com",
                "Camila Rojas",
                UserAccountType.HOME,
                List.of(
                        new PlantData("Monstera Grande", "Monstera deliciosa", "Sala de estar"),
                        new PlantData("Pothos Dorado", "Epipremnum aureum", "Dormitorio principal"),
                        new PlantData("Ficus Lyrata", "Ficus lyrata", "Oficina")));

        // Usuario 2: Vivero forestal profesional
        seedUserWithPlants(
                "martin.diaz@plantae.com",
                "Martín Díaz",
                UserAccountType.VIVERO_FORESTAL,
                List.of(
                        new PlantData("Orquídea Phalaenopsis #1", "Phalaenopsis spp.", "Invernadero A"),
                        new PlantData("Orquídea Phalaenopsis #2", "Phalaenopsis spp.", "Invernadero A"),
                        new PlantData("Hortensia Azul", "Hydrangea macrophylla", "Invernadero B"),
                        new PlantData("Rosa Miniatura", "Rosa chinensis 'Minima'", "Zona de propagación")));

        // Usuario 3: Huerto urbano
        seedUserWithPlants(
                "sofia.martinez@plantae.com",
                "Sofía Martínez",
                UserAccountType.HOME,
                List.of(
                        new PlantData("Tomates Cherry", "Solanum lycopersicum var. cerasiforme", "Balcón"),
                        new PlantData("Albahaca Genovesa", "Ocimum basilicum", "Cocina"),
                        new PlantData("Menta Fresca", "Mentha spicata", "Cocina"),
                        new PlantData("Romero Aromático", "Salvia rosmarinus", "Terraza")));

        // Usuario 4: Coleccionista de suculentas
        seedUserWithPlants(
                "lucas.fernandez@plantae.com",
                "Lucas Fernández",
                UserAccountType.HOME,
                List.of(
                        new PlantData("Aloe Vera Grande", "Aloe vera", "Balcón"),
                        new PlantData("Árbol de Jade", "Crassula ovata", "Sala de estar"),
                        new PlantData("Echeveria Elegans", "Echeveria elegans", "Dormitorio principal"),
                        new PlantData("Sansevieria", "Dracaena trifasciata", "Oficina"),
                        new PlantData("Cola de Burro", "Sedum morganianum", "Terraza")));

        // Usuario 5: Vivero forestal (producción)
        seedUserWithPlants(
                "ana.garcia@plantae.com",
                "Ana García",
                UserAccountType.VIVERO_FORESTAL,
                List.of(
                        new PlantData("Monstera Adansonii", "Monstera adansonii", "Invernadero A"),
                        new PlantData("Philodendron", "Philodendron hederaceum", "Invernadero A"),
                        new PlantData("Anturio Rojo", "Anthurium andraeanum", "Invernadero B"),
                        new PlantData("Cuna de Moisés", "Spathiphyllum wallisii", "Invernadero B"),
                        new PlantData("ZZ Plant", "Zamioculcas zamiifolia", "Zona de propagación")));

        // Usuario 6: Oficina verde
        seedUserWithPlants(
                "pedro.sanchez@plantae.com",
                "Pedro Sánchez",
                UserAccountType.HOME,
                List.of(
                        new PlantData("Planta ZZ", "Zamioculcas zamiifolia", "Oficina"),
                        new PlantData("Cinta Verde", "Chlorophytum comosum", "Sala de estar"),
                        new PlantData("Hiedra Inglesa", "Hedera helix", "Balcón")));
    }

    private void seedUserWithPlants(String email, String name, UserAccountType type, List<PlantData> plants) {
        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            return;
        }

        // Crear usuario
        User user = User.register(
                email,
                "Demo1234!",
                name,
                UserLanguage.ES,
                type,
                passwordHasher::hash);
        userRepository.save(user);

        // Crear perfil público con slug
        createProfileForUser(user);

        Instant now = Instant.now();
        int plantIndex = 0;

        for (PlantData plantData : plants) {
            // Crear planta
            Plant plant = Plant.create(
                    user.getId().value(),
                    plantData.name(),
                    plantData.species(),
                    plantData.location(),
                    null);
            plantRepository.save(plant);

            // Crear sensor y vincularlo
            Sensor sensor = Sensor.register(SensorType.MULTI, user.getId().value(), plant.getId());
            sensorRepository.save(sensor);
            plant.assignSensor(sensor.getId().value());
            plantRepository.save(plant);

            // Generar 30 días de lecturas históricas
            generateHistoricalReadings(sensor, now, plantData);

            // Generar alertas aleatorias
            generateAlerts(plant, sensor, user.getId().value(), now, plantIndex);

            plantIndex++;
        }
    }

    private void generateHistoricalReadings(Sensor sensor, Instant now, PlantData plantData) {
        // Generar lecturas para los últimos 30 días
        for (int day = 0; day < 30; day++) {
            Instant dayStart = now.minus(day, ChronoUnit.DAYS);

            // 4 lecturas por día para cada métrica
            for (int hour = 0; hour < 24; hour += 6) {
                Instant timestamp = dayStart.minus(hour, ChronoUnit.HOURS);

                // Temperatura: varía según la hora (más frío de noche)
                double tempBase = hour < 12 ? 18 : 24;
                double temperature = tempBase + randomVariation(4);
                sensorReadingRepository.save(new SensorReading(
                        sensor.getId(), timestamp, SensorMetric.TEMPERATURE,
                        temperature, getQuality(temperature, 15, 30)));

                // Humedad del suelo: decrece con el tiempo
                double soilBase = 60 - (day * 1.5); // Decrece cada día
                double soilMoisture = Math.max(10, soilBase + randomVariation(10));
                sensorReadingRepository.save(new SensorReading(
                        sensor.getId(), timestamp.plus(1, ChronoUnit.HOURS),
                        SensorMetric.SOILMOISTURE, soilMoisture, getQuality(soilMoisture, 20, 80)));

                // Humedad ambiental
                double humidity = 55 + randomVariation(20);
                sensorReadingRepository.save(new SensorReading(
                        sensor.getId(), timestamp.plus(2, ChronoUnit.HOURS),
                        SensorMetric.HUMIDITY, humidity, getQuality(humidity, 30, 70)));

                // Luz: varía según la hora del día
                double lightBase = hour >= 6 && hour <= 18 ? 500 : 50;
                double light = lightBase + randomVariation(200);
                sensorReadingRepository.save(new SensorReading(
                        sensor.getId(), timestamp.plus(3, ChronoUnit.HOURS),
                        SensorMetric.LIGHT, Math.max(0, light), "OK"));
            }
        }

        // Agregar lecturas recientes (últimas 6 horas)
        for (int h = 0; h < 6; h++) {
            Instant recentTime = now.minus(h, ChronoUnit.HOURS);
            sensorReadingRepository.save(new SensorReading(
                    sensor.getId(), recentTime, SensorMetric.TEMPERATURE,
                    22 + randomVariation(3), "OK"));
            sensorReadingRepository.save(new SensorReading(
                    sensor.getId(), recentTime.plus(15, ChronoUnit.MINUTES),
                    SensorMetric.SOILMOISTURE, 45 + randomVariation(15), "OK"));
            sensorReadingRepository.save(new SensorReading(
                    sensor.getId(), recentTime.plus(30, ChronoUnit.MINUTES),
                    SensorMetric.HUMIDITY, 60 + randomVariation(10), "OK"));
            sensorReadingRepository.save(new SensorReading(
                    sensor.getId(), recentTime.plus(45, ChronoUnit.MINUTES),
                    SensorMetric.LIGHT, 400 + randomVariation(200), "OK"));
        }
    }

    private void generateAlerts(Plant plant, Sensor sensor, String userId, Instant now, int plantIndex) {
        // Cada tercera planta tiene una alerta activa
        if (plantIndex % 3 == 0) {
            Alert activeAlert = Alert.raise(plant.getId(), sensor.getId(), userId, AlertType.THRESHOLD_BREACH);
            activeAlert.addMetadata("metric", SensorMetric.SOILMOISTURE.apiName());
            activeAlert.addMetadata("value", 15.0);
            activeAlert.addMetadata("threshold", 20.0);
            activeAlert.addMetadata("message", "Humedad del suelo baja - Regar pronto");
            alertRepository.save(activeAlert);
            plant.setHasAlerts(true);
            plantRepository.save(plant);
        }

        // Cada cuarta planta tiene una alerta de sensor inactivo (resuelta)
        if (plantIndex % 4 == 0) {
            Alert oldAlert = Alert.raise(plant.getId(), sensor.getId(), userId, AlertType.SENSOR_INACTIVE);
            oldAlert.addMetadata("note", "Sensor sin datos por 24h - Resuelto automáticamente");
            oldAlert.resolve();
            alertRepository.save(oldAlert);
        }

        // Cada quinta planta tiene una alerta de temperatura alta (resuelta)
        if (plantIndex % 5 == 0) {
            Alert tempAlert = Alert.raise(plant.getId(), sensor.getId(), userId, AlertType.THRESHOLD_BREACH);
            tempAlert.addMetadata("metric", SensorMetric.TEMPERATURE.apiName());
            tempAlert.addMetadata("value", 35.0);
            tempAlert.addMetadata("threshold", 30.0);
            tempAlert.addMetadata("message", "Temperatura alta detectada");
            tempAlert.resolve();
            alertRepository.save(tempAlert);
        }
    }

    private double randomVariation(double range) {
        return (random.nextDouble() - 0.5) * range;
    }

    private String getQuality(double value, double low, double high) {
        if (value < low)
            return "LOW";
        if (value > high)
            return "HIGH";
        return "OK";
    }

    private void createProfileForUser(User user) {
        // Verificar si ya existe un perfil para este usuario
        UserId ownerId = user.getId();
        if (profileRepository.findByOwnerId(ownerId).isPresent()) {
            return;
        }

        // Generar slug único basado en el nombre
        String baseSlug = toSlug(user.getDisplayName());
        String finalSlug = baseSlug;

        // Si el slug ya existe, agregar un número
        int counter = 1;
        while (profileRepository.existsBySlug(ProfileSlug.of(finalSlug))) {
            finalSlug = baseSlug + "-" + counter;
            counter++;
        }

        // Crear y guardar el perfil
        final ProfileSlug profileSlug = ProfileSlug.of(finalSlug);
        Profile profile = Profile.create(ownerId, user.getDisplayName(), name -> profileSlug);
        profile.updatePublicFields(
                user.getDisplayName(),
                "Apasionado por las plantas y el cuidado del verde. 🌿",
                null,
                "Lima, Perú",
                "America/Lima");
        profileRepository.save(profile);
    }

    private String toSlug(String input) {
        if (input == null || input.isBlank()) {
            return "user-" + System.currentTimeMillis();
        }
        // Normalizar acentos y caracteres especiales
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        // Convertir a minúsculas, reemplazar espacios por guiones, eliminar caracteres
        // no válidos
        return normalized.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }

    private record PlantData(String name, String species, String location) {
    }
}
