import java.util.*; 
import java.util.*; 

/**
 * Compares two {@link ChargingStation} objects based on their ID in ascending order.
 * This can be used for sorting lists of charging stations.
 * @author DP Clasess
 * @version 2023
 */
public class ComparatorChargingStationId implements Comparator<ChargingStation>
{
    /**
     * Compares its two {@link ChargingStation} arguments for order.
     * The comparison is based on the lexicographical order of their IDs.
     * @param st1 The first charging station to be compared.
     * @param st2 The second charging station to be compared.
     * @return A negative integer, zero, or a positive integer as the first
     * station's ID is less than, equal to, or greater than the second.
     */
    public int compare(ChargingStation st1, ChargingStation st2){  
        //TODO: Complete this code
        return 0;   
    } 
}