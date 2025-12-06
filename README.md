# backendNew-PlantaE

## Demo data
- Se crean dos usuarios demo al iniciar el backend (CommandLineRunner):
  - HOME: `camila.rojas@plantae.com`
  - VIVERO_FORESTAL: `martin.diaz@plantae.com`
  - Contraseña para ambos: `Demo1234!`
- Cada usuario tiene:
  - 1 planta (Monstera), 1 dispositivo ficticio, 1 sensor MULTI vinculado.
  - Lecturas históricas de los últimos 7 días + lecturas recientes para PDF/alertas.
  - Alertas de ejemplo: una resuelta por inactividad y una activa por umbral de humedad.
- Los reportes PDF sin rango usan por defecto las últimas 72h; con los datos cargados se mostrarán métricas recientes.
