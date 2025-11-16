

import java.util.Comparator;

/**
 * Compara dos {@link ElectricVehicle} para el informe final.
 * Criterio 1: Turno de llegada (arrivingStep) - Ascendente.
 * Criterio 2: Matrícula (plate) - Ascendente (para desempate).
 * Requerido por el PDF.
 */
public class ComparatorEVArrivingStep implements Comparator<ElectricVehicle>
{
    /**
     * Compara dos vehículos por turno de llegada y luego matrícula.
     * @param ev1 El primer vehículo.
     * @param ev2 El segundo vehículo.
     * @return -1, 0, o 1.
     */
    public int compare(ElectricVehicle ev1, ElectricVehicle ev2){  
        
        // --- Criterio 1: Turno de Llegada (Ascendente) ---
        // (ev1 - ev2) da negativo si ev1 llegó antes
        int stepCompare = ev1.getArrivingStep() - ev2.getArrivingStep();
        if (stepCompare != 0) {
            return stepCompare;
        }

        // --- Criterio 2: Matrícula (Ascendente) ---
        // (Si empataron en turno, desempata la matrícula)
        return ev1.getPlate().compareTo(ev2.getPlate());
    } 
}