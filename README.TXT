=========================================
 PROYECTO: EV-Charging-Station-Simulation
=========================================

== 1. Miembros del Grupo ==

Este proyecto ha sido realizado por:
* Carlos Sánchez Sánchez

== 2. Clase de Ejecución ==

La clase que se debe ejecutar para realizar la simulación es: EVDemo

En el entorno BlueJ, la simulación se inicia haciendo clic derecho sobre la clase EVDemo y seleccionando el método:
-> void main()


== 3. Extras del Proyecto Implementados ==

En este proyecto, se han implementado los siguientes requisitos extras (definidos en el apartado 4.d del enunciado del proyecto):

* **Uso correcto de Colecciones y Comparadores:** 
    * Se utiliza `Collections.sort()` para ordenar las listas de vehículos y estaciones en la clase `EVDemo`.
    * Se han implementado todos los comparadores solicitados por el PDF:
        * Un comparador para `ElectricVehicle` por matrícula (ascendente): `ComparatorEVPlate`.
        * Un comparador para `ElectricVehicle` por turno de llegada (ascendente) con desempate por matrícula: `ComparatorEVArrivingStep`.
        * Un comparador para `ChargingStation` por número de recargas (descendente) con desempate por ID: `ComparatorChargingStationNumberRecharged`.
        * Un comparador para `Charger` por velocidad (descendente), tarifa (ascendente) e ID (ascendente): `ComparatorChargersId`.

* **Pruebas unitarias del proyecto con JUnit:**
    * Se ha completado el fichero de pruebas `LocationTest.java`.
    * Se ha probado el método `distance` (con `testDistance`)[cite: 335].
    * Se ha probado el método `nextLocation` (con `testAdjacentLocations` y `testNonAdjacentLocations`).

* **Uso de la funcionalidad de Git:**
    * El proyecto ha sido inicializado con Git y subido a un repositorio remoto (GitHub) para permitir el desarrollo colaborativo del equipo.
