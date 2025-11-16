import java.util.*; 

/**
 * Compares two {@link Charger} objects based on their **ID** string in **ascending** order.
 * This is typically used as a final tie-breaker in multi-criteria sorting.
 * * @author DP Clasess
 * @version 2024.10.07
 */
public class ComparatorChargersId implements Comparator<Charger>
{
    /**
     * Compares its two {@link Charger} arguments for order.
     * The comparison is based on the lexicographical order of their IDs.
     * @param c1 The first charger to be compared.
     * @param c2 The second charger to be compared.
     * @return A negative integer, zero, or a positive integer as the first 
     * charger's ID is lexicographically less than, equal to, or greater than the second.
     */
    public int compare(Charger c1, Charger c2){  
        
        // --- Criterio 1: Velocidad de Carga (Descendente) ---
        int speedCompare = c2.getChargingSpeed() - c1.getChargingSpeed();
        if (speedCompare != 0) {
            return speedCompare;
        }
        
        // --- Criterio 2: Tarifa (Ascendente) ---
        int feeCompare = Float.compare(c1.getChargingFee(), c2.getChargingFee());
        if (feeCompare != 0) {
            return feeCompare;
        }

        // --- Criterio 3: ID (Ascendente) ---
        return c1.getId().compareTo(c2.getId());

    }
}