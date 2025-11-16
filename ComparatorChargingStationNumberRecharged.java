import java.util.*; 

/**
 * Compares two {@link ChargingStation} objects based primarily on the number of
 * {@link ElectricVehicle}s recharged in descending order.
 * If the number of recharges is equal, it uses {@link ComparatorChargingStationId}
 * to compare by ID in ascending order as a tie-breaker.
 * * @author DP Clasess
 * @version 2023
 */
public class ComparatorChargingStationNumberRecharged implements Comparator<ChargingStation>
{
    /**
     * Compares its two {@link ChargingStation} arguments for order.
     * * @param st1 The first charging station to be compared.
     * @param st2 The second charging station to be compared.
     * @return A negative integer if st1 has more recharges or an equal number but smaller ID, 
     * zero if both are equal, or a positive integer otherwise.
     */
    public int compare(ChargingStation st1, ChargingStation st2){  
         if (st1.getNumerEVRecharged() > st2.getNumerEVRecharged())
            return -1;
        else if (st1.getNumerEVRecharged() < st2.getNumerEVRecharged())
            return 1;
        else return (new ComparatorChargingStationId().compare(st1,st2));
    } 
}