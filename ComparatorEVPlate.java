// En el fichero NUEVO: ComparatorEVPlate.java

import java.util.Comparator; // ¡No olvides el import!

/**
 * Compara dos {@link ElectricVehicle} por su matrícula (plate)
 * en orden ascendente (alfabético).
 * Esta clase es necesaria para cumplir el requisito del PDF .
 */
public class ComparatorEVPlate implements Comparator<ElectricVehicle>
{
    /**
     * Compara dos vehículos por su matrícula.
     * @param ev1 El primer vehículo.
     * @param ev2 El segundo vehículo.
     * @return un número negativo, 0, o positivo.
     */
    public int compare(ElectricVehicle ev1, ElectricVehicle ev2){  
        
        // Compara el String de la matrícula de ev1 con el de ev2
        return ev1.getPlate().compareTo(ev2.getPlate());
    } 
}