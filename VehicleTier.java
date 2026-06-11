/**
 * Defines tiers/types of electric vehicles for charger access rules.
 */
public enum VehicleTier {
    PRIORITY, STANDARD, VTC, PREMIUM, DELIVERY;
    
    public static int numTiers(){
        return VehicleTier.values().length;
    }
    

}
