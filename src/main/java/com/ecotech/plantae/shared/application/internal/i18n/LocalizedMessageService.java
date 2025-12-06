package com.ecotech.plantae.shared.application.internal.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LocalizedMessageService {

    private final Map<String, Map<String, String>> messages = new HashMap<>();

    public LocalizedMessageService() {
        Map<String, String> en = Map.ofEntries(
                Map.entry("iam.register.success", "Account created successfully"),
                Map.entry("iam.login.success", "Login successful"),
                Map.entry("iam.login.invalid", "Invalid credentials. Please check your email and password."),
                Map.entry("errors.validation", "Please review the highlighted fields."),
                Map.entry("errors.password.size", "Password must be between 8 and 100 characters."),
                Map.entry("errors.password.notblank", "Password is required."),
                Map.entry("errors.email.notblank", "Email is required."),
                Map.entry("errors.email.email", "Enter a valid email address."),
                Map.entry("errors.token.notblank", "Token is required."),
                Map.entry("iam.forgot.success", "If the account exists you will receive recovery instructions"),
                Map.entry("iam.reset.success", "Password reset successfully"),
                Map.entry("iam.reset.invalid", "Invalid or expired reset link"),
                Map.entry("iam.reset.email.subject", "Reset your PlantaE password"),
                Map.entry("iam.reset.email.body",
                        "You requested to reset your PlantaE password. Use the secure link below within the next 15 minutes to choose a new password.\n\nRecibiste este correo porque solicitaste restablecer tu contraseña de PlantaE. Usa el enlace seguro a continuación dentro de los próximos 15 minutos para elegir una nueva contraseña.\n\nReset link: {link}"),
                Map.entry("iam.change.success", "Password updated successfully"),
                Map.entry("iam.delete.success", "Account deleted successfully"),
                Map.entry("iam.profile.success", "Profile retrieved successfully"),
                Map.entry("profile.get.success", "Profile preferences retrieved successfully"),
                Map.entry("profile.update.success", "Profile preferences updated successfully"),
                Map.entry("profile.notifications.get.success", "Notification settings retrieved successfully"),
                Map.entry("profile.notifications.update.success", "Notification settings updated successfully"),
                Map.entry("plant.delete.success", "Plant deleted successfully"),
                Map.entry("plant.update.success", "Plant updated successfully"),
                Map.entry("plant.species.invalid", "Invalid species. Please choose one from the catalog."),
                Map.entry("sensor.plant.required", "Plant is required to register a sensor."),
                Map.entry("sensor.plant.notfound", "Plant not found."),
                Map.entry("alerts.THRESHOLD_BREACH.title", "Threshold breach"),
                Map.entry("alerts.SENSOR_INACTIVE.title", "Sensor inactive"),
                Map.entry("alerts.DEVICE_DEACTIVATED.title", "Device deactivated"),
                Map.entry("alerts.WEEKLY_REPORT.title", "Weekly report"),
                Map.entry("alerts.MONTHLY_REPORT.title", "Monthly report"),
                Map.entry("home.manual-action.saved", "Manual action registered"),
                Map.entry("nursery.input.saved", "Input registered")
        );
        Map<String, String> es = Map.ofEntries(
                Map.entry("iam.register.success", "Cuenta creada exitosamente"),
                Map.entry("iam.login.success", "Inicio de sesión exitoso"),
                Map.entry("iam.login.invalid", "Credenciales incorrectas. Verifica tu correo y contraseña."),
                Map.entry("errors.validation", "Por favor revisa los campos resaltados."),
                Map.entry("errors.password.size", "La contraseña debe tener entre 8 y 100 caracteres."),
                Map.entry("errors.password.notblank", "La contraseña es obligatoria."),
                Map.entry("errors.email.notblank", "El correo es obligatorio."),
                Map.entry("errors.email.email", "Ingresa un correo válido."),
                Map.entry("errors.token.notblank", "El token es obligatorio."),
                Map.entry("iam.forgot.success", "Si la cuenta existe recibirás instrucciones de recuperación"),
                Map.entry("iam.reset.success", "Contraseña restablecida correctamente"),
                Map.entry("iam.reset.invalid", "Enlace de restablecimiento inválido o expirado"),
                Map.entry("iam.reset.email.subject", "Restablece tu contraseña de PlantaE"),
                Map.entry("iam.reset.email.body",
                        "Solicitaste restablecer tu contraseña de PlantaE. Usa el enlace seguro a continuación dentro de los próximos 15 minutos para elegir una nueva contraseña.\n\nYou received this email because you requested to reset your PlantaE password. Use the secure link below within the next 15 minutes to choose a new password.\n\n{link}"),
                Map.entry("iam.change.success", "Contraseña actualizada exitosamente"),
                Map.entry("iam.delete.success", "Cuenta eliminada exitosamente"),
                Map.entry("iam.profile.success", "Perfil obtenido exitosamente"),
                Map.entry("profile.get.success", "Preferencias de perfil obtenidas correctamente"),
                Map.entry("profile.update.success", "Preferencias de perfil actualizadas correctamente"),
                Map.entry("profile.notifications.get.success", "Preferencias de notificaciones obtenidas correctamente"),
                Map.entry("profile.notifications.update.success", "Preferencias de notificaciones actualizadas correctamente"),
                Map.entry("plant.delete.success", "Planta eliminada correctamente"),
                Map.entry("plant.update.success", "Planta actualizada correctamente"),
                Map.entry("plant.species.invalid", "Especie inválida. Elige una de la lista del catálogo."),
                Map.entry("sensor.plant.required", "La planta es obligatoria para registrar un sensor."),
                Map.entry("sensor.plant.notfound", "No se encontró la planta."),
                Map.entry("alerts.THRESHOLD_BREACH.title", "Umbral excedido"),
                Map.entry("alerts.SENSOR_INACTIVE.title", "Sensor inactivo"),
                Map.entry("alerts.DEVICE_DEACTIVATED.title", "Dispositivo desactivado"),
                Map.entry("alerts.WEEKLY_REPORT.title", "Reporte semanal"),
                Map.entry("alerts.MONTHLY_REPORT.title", "Reporte mensual"),
                Map.entry("home.manual-action.saved", "Acción manual registrada"),
                Map.entry("nursery.input.saved", "Aplicación de insumo registrada")
        );
        messages.put("en", en);
        messages.put("es", es);
    }

    public String getMessage(String key, Locale locale) {
        String lang = locale == null ? "en" : locale.getLanguage();
        return messages.getOrDefault(lang, messages.get("en")).getOrDefault(key, key);
    }

    public Map<String, String> getCatalog(String namespace, Locale locale) {
        String lang = locale == null ? "en" : locale.getLanguage();
        Map<String, String> langMessages = messages.getOrDefault(lang, messages.get("en"));
        return langMessages.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(namespace + "."))
                .collect(HashMap::new, (map, entry) -> map.put(entry.getKey().substring(namespace.length() + 1), entry.getValue()), Map::putAll);
    }
}
