=========================================
 PROYECTO: EV-Charging-Station-Simulation
=========================================

== 1. Autor ==

* Carlos Sánchez Sánchez

== 2. Descripción breve ==

Este proyecto simula la operación de una red de carga para vehículos eléctricos con:
- Vehículos (StandardEV, PriorityEV, VtcEV, PremiumEV)
- Estaciones de carga y cargadores (StandardCharger, SolarCharger, UltraFastCharger, PriorityCharger)
- Lógica de movimiento y consumo de batería
- Comparadores y ordenación de listas
- Pruebas unitarias con JUnit

== 3. Requisitos previos ==

1. Tener Java JDK instalado (Java 8 o superior).
2. Tener BlueJ instalado (versión recomendada: 4.x o superior).
3. El proyecto debe estar descomprimido en una carpeta local, p. ej.:
   `C:\Users\Carlos\DirectorioBlueJ\EVChargingInitial`

== 4. Apertura en BlueJ ==

1. Abrir BlueJ.
2. Seleccionar "Project > Open Project...".
3. Navegar a `C:\Users\Carlos\DirectorioBlueJ\EVChargingInitial` y abrir la carpeta.
4. BlueJ debe listar las clases: `EVDemo`, `ElectricVehicle`, `ChargingStation`, `Charger`, etc.

== 5. Compilación ==

- En BlueJ, haga clic en el botón "Compile" (o compile proyecto completo si aporta esa opción).
- Asegúrate de que no hay errores en la compilación.

== 6. Ejecución de la simulación ==

1. En el diagrama de clases de BlueJ, haz clic derecho en `EVDemo`.
2. Selecciona el método estático `void main(String[] args)` (a veces aparece como "main").
3. Si BlueJ solicita argumentos, puede dejarse vacío y pulsar OK (si el diseño del proyecto no requiere entradas por línea de comandos).
4. Observa la salida en la consola de BlueJ.

== 7. Pruebas unitarias (JUnit) ==

1. En el proyecto hay clases de prueba con sufijo `Test`, como:
   - `LocationTest` (ya implementada).
   - `ElectricVehicleTest`, `ChargingStationTest`, etc. (posiblemente existente o por implementar).
2. Para ejecutar cada prueba:
   - Haz clic derecho sobre la clase de prueba (p.ej. `LocationTest`).
   - Selecciona "Test" (según la versión de BlueJ) o el constructor de pruebas `run`.
3. Comprueba los resultados de los test en la consola de BlueJ.

== 8. Estructura del código (claves) ==

- `ElectricVehicle.java`: clase base (quizá abstracta) con atributos comunes: matrícula, energía, consumo, ubicación.
- `StandardEV.java`, `PremiumEV.java`, `PriorityEV.java`, `VtcEV.java`: variantes de vehículo con comportamientos específicos.
- `ChargingStation.java`: gestiona un conjunto de `Charger` y recargas de los vehículos.
- `Charger.java`: clase padre de `StandardCharger`, `SolarCharger`, `UltraFastCharger`, `PriorityCharger`.
- Comparadores:
  * `ComparatorEVPlate` (matrícula ascendente).
  * `ComparatorEVArrivingStep` (turno llegada, desempate por matrícula).
  * `ComparatorChargingStationNumberRecharged` (número recargas, desempate por ID).
  * `ComparatorChargersId` (velocidad, tarifa, id).

== 9. Ejemplos de uso en EVDemo ==

- Crea objetos `ElectricVehicle` y los asigna a estaciones.
- Simula pasos de recarga y movimiento.
- Muestra resultados por consola con operaciones `System.out.println`.

== 10. Mantenimiento y extensión ==

- Añadir nuevos tipos de vehículo en `ElectricVehicle` + clase derivada.
- Añadir nueva lógica de ruta en `Location`.
- Mejorar prioridad de asignación en `ChargingStation`.
- Añadir tests en la carpeta del proyecto con nuevos casos de uso.

== 11. Problemas comunes y soluciones ==

- "NoClassDefFoundError" o clases en rojo: asegúrate de que el proyecto está en la carpeta correcta y no hay files duplicados.
- Tests que fallan por valores esperados: revisa los constructores de `Location` y casos de consumo energético.
- Compilación fallida en BlueJ por clase abstracta: usa clases concretas (`StandardEV`, `PriorityEV`) para crear instancias.

== 12. Contacto ==

Proyecto desarrollado por: Carlos Sánchez Sánchez

---

¡Disfruta probando la simulación de estaciones de carga para vehículos eléctricos en BlueJ!
